package com.dolan.arif.dnsqlite

import android.provider.BaseColumns

/**
 * Created by Bencoleng on 17/09/2019.
 */
class DatabaseContract {

    companion object {
        const val TABLE_NOTE = "note"

        class NoteColumn : BaseColumns {
            companion object : KBaseColumns() {
                const val TITLE = "title"
                const val DESC = "desc"
            }
        }
    }
}

open class KBaseColumns {
    val _ID = "_id"
}