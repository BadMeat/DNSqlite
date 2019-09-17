package com.dolan.arif.dnsqlite

import android.content.ContentValues
import android.content.Context
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import android.provider.BaseColumns._ID

/**
 * Created by Bencoleng on 17/09/2019.
 */
class NoteHelper(context: Context) {

    var databaseHelper: DatabaseHelper = DatabaseHelper(context)

    private var database: SQLiteDatabase? = null

    companion object {
        const val DATABASE_TABLE = DatabaseContract.TABLE_NOTE

        var instance: NoteHelper? = null

        fun getInstance(ctx: Context): NoteHelper {
            if (instance == null) {
                instance = NoteHelper(ctx)
            }
            return instance as NoteHelper
        }
    }

    @Throws(SQLException::class)
    fun open() {
        database = databaseHelper.writableDatabase
    }

    fun close() {
        databaseHelper.close()
        if (database != null) {
            if (database!!.isOpen) {
                database?.close()
            }
        }
    }

    fun getAllNotes(): List<Note> {
        val noteList: MutableList<Note> = mutableListOf()
        val cursor = database?.query(
            DATABASE_TABLE, null,
            null,
            null,
            null,
            null,
            "$_ID ASC",
            null
        )
        cursor?.moveToFirst()
        var note: Note
        if (cursor != null) {
            if (cursor.count > 0) {
                do {
                    note = Note(
                        cursor.getInt(cursor.getColumnIndexOrThrow(_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.Companion.NoteColumn.TITLE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.Companion.NoteColumn.DESC))
                    )
                    noteList.add(note)
                    cursor.moveToNext()
                } while (!cursor.isAfterLast)
            }
            cursor.close()
        }

        return noteList
    }

    fun insertNote(note: Note): Long? {
        val args = ContentValues()
        args.put(DatabaseContract.Companion.NoteColumn.TITLE, note.title)
        args.put(DatabaseContract.Companion.NoteColumn.DESC, note.desc)
        return database?.insert(DATABASE_TABLE, null, args)
    }

    fun updateNote(note: Note): Int? {
        val args = ContentValues()
        args.put(DatabaseContract.Companion.NoteColumn.TITLE, note.title)
        args.put(DatabaseContract.Companion.NoteColumn.DESC, note.desc)
        return database?.update(DATABASE_TABLE, args, "$_ID = '${note.id}'", null)
    }

    fun deleteNote(id: Int): Int? {
        return database?.delete(DATABASE_TABLE, "$_ID = '${id}'", null)
    }

}