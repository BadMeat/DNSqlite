package com.dolan.arif.dnsqlite

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

/**
 * Created by Bencoleng on 17/09/2019.
 */
class DatabaseHelper(ctx: Context) : SQLiteOpenHelper(ctx, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(SQLITE_CREATE_TABLE_NOTE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
        db?.execSQL("DROP TABLE IF EXISTS ${DatabaseContract.TABLE_NOTE}")
        onCreate(db)
    }

    companion object {
        const val DATABASE_NAME = "dbnoteapp"
        const val DATABASE_VERSION = 1

        var SQLITE_CREATE_TABLE_NOTE = String.format(
            "CREATE TABLE %s"
                    + " (%s INTEGER PRIMARY KEY AUTOINCREMENT," +
                    " %s TEXT NOT NULL," +
                    " %s TEXT NOT NULL)",
            DatabaseContract.TABLE_NOTE,
            DatabaseContract.Companion.NoteColumn._ID,
            DatabaseContract.Companion.NoteColumn.TITLE,
            DatabaseContract.Companion.NoteColumn.DESC
        )
    }

}