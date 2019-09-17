package com.dolan.arif.dnsqlite.helper

import android.database.Cursor
import android.provider.BaseColumns._ID
import com.dolan.arif.dnsqlite.DatabaseContract.Companion.NoteColumn.Companion.DESC
import com.dolan.arif.dnsqlite.DatabaseContract.Companion.NoteColumn.Companion.TITLE
import com.dolan.arif.dnsqlite.Note

class MappingHelper {

    companion object {
        fun mapCursorToArrayList(noteCursor: Cursor): List<Note> {
            val noteList = mutableListOf<Note>()

            while (noteCursor.moveToNext()) {
                val id = noteCursor.getInt(noteCursor.getColumnIndexOrThrow(_ID))
                val title = noteCursor.getString(noteCursor.getColumnIndexOrThrow(TITLE))
                val desc = noteCursor.getString(noteCursor.getColumnIndexOrThrow(DESC))
                noteList.add(Note(id, title, desc))
            }
            return noteList
        }
    }
}