package com.dolan.arif.dnsqlite

import android.database.Cursor
import android.os.Parcelable
import android.provider.BaseColumns._ID
import kotlinx.android.parcel.Parcelize

/**
 * Created by Bencoleng on 17/09/2019.
 */
@Parcelize
data class Note(
    var id: Int? = 0,
    var title: String? = "Title",
    var desc: String? = "Des"
) : Parcelable {
    constructor(cursor: Cursor) : this() {
        id = DatabaseContract.getColumnInt(cursor, _ID)
        title =
            DatabaseContract.getColumnString(cursor, DatabaseContract.Companion.NoteColumn.TITLE)
        desc = DatabaseContract.getColumnString(cursor, DatabaseContract.Companion.NoteColumn.DESC)
    }
}