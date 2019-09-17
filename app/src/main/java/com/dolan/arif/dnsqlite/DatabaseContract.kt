package com.dolan.arif.dnsqlite

import android.database.Cursor
import android.net.Uri
import android.provider.BaseColumns

/**
 * Created by Bencoleng on 17/09/2019.
 */
class DatabaseContract {

    companion object {
        const val TABLE_NOTE = "note"

        const val AUTHORITY = "com.dolan.arif.dnsqlite"
        const val SCHEME = "content"

        class NoteColumn : BaseColumns {
            companion object : KBaseColumns() {
                const val TITLE = "title"
                const val DESC = "desc"

                val CONTENT_URI = Uri.Builder().scheme(SCHEME)
                    .authority(AUTHORITY)
                    .appendPath(TABLE_NOTE)
                    .build()
            }
        }

        fun getColumnString(cursor: Cursor, columnName: String): String {
            return cursor.getString(cursor.getColumnIndexOrThrow(columnName))
        }

        fun getColumnInt(cursor: Cursor, columnName: String): Int {
            return cursor.getInt(cursor.getColumnIndexOrThrow(columnName))
        }

        fun getColumnLong(cursor: Cursor, columnName: String): Long {
            return cursor.getLong(cursor.getColumnIndexOrThrow(columnName))
        }
    }
}

open class KBaseColumns {
    val _ID = "_id"
}