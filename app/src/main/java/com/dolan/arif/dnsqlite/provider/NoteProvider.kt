package com.dolan.arif.dnsqlite.provider

import android.content.ContentProvider
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import android.os.Handler
import com.dolan.arif.dnsqlite.DatabaseContract.Companion.AUTHORITY
import com.dolan.arif.dnsqlite.DatabaseContract.Companion.NoteColumn.Companion.CONTENT_URI
import com.dolan.arif.dnsqlite.DatabaseContract.Companion.TABLE_NOTE
import com.dolan.arif.dnsqlite.MainActivity
import com.dolan.arif.dnsqlite.NoteHelper

class NoteProvider : ContentProvider() {

    private var noteHelper: NoteHelper? = null

    companion object {
        const val NOTE = 1
        const val NOTE_ID = 2
        var uriMathcer = UriMatcher(UriMatcher.NO_MATCH)

        init {
            // content://com.dicoding.picodiploma.mynotesapp/note
            uriMathcer.addURI(AUTHORITY, TABLE_NOTE, NOTE)

            // content://com.dicoding.picodiploma.mynotesapp/note/id
            uriMathcer.addURI(AUTHORITY, "$TABLE_NOTE/#", NOTE_ID)
        }
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        noteHelper?.open()
        val add: Long? = when (uriMathcer.match(uri)) {
            NOTE -> {
                noteHelper?.insertProvider(values!!)
            }
            else -> {
                0
            }
        }
        context?.contentResolver?.notifyChange(
            CONTENT_URI,
            MainActivity.DataObserver(Handler(), context!!)
        )
        return Uri.parse("$CONTENT_URI/$add")
    }

    override fun query(
        uri: Uri,
        projection: Array<String>?,
        selection: String?,
        selectionArgs: Array<String>?,
        sortOrder: String?
    ): Cursor? {
        noteHelper?.open()
        val cursor: Cursor?
        when (uriMathcer.match(uri)) {
            NOTE -> {
                cursor = noteHelper?.queryProvider()
            }
            NOTE_ID -> {
                cursor = noteHelper?.queryByIdProvider(uri.lastPathSegment!!)
            }
            else -> {
                cursor = null
            }
        }
        return cursor
    }

    override fun onCreate(): Boolean {
        noteHelper = NoteHelper.getInstance(context!!)
        return true
    }

    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<String>?
    ): Int {
        noteHelper?.open()
        val updated: Int? = when (uriMathcer.match(uri)) {
            NOTE_ID -> {
                noteHelper?.updateProvider(uri.lastPathSegment!!, values!!)
            }
            else -> {
                0
            }
        }
        context?.contentResolver?.notifyChange(
            CONTENT_URI,
            MainActivity.DataObserver(Handler(), context!!)
        )
        return updated!!
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        noteHelper?.open()
        val deleted = when (uriMathcer.match(uri)) {
            NOTE_ID -> {
                noteHelper?.deleteProvider(uri.lastPathSegment!!)
            }
            else -> {
                0
            }
        }
        context?.contentResolver?.notifyChange(
            CONTENT_URI,
            MainActivity.DataObserver(Handler(), context!!)
        )
        return deleted!!
    }

    override fun getType(uri: Uri): String? {
        return null
    }
}