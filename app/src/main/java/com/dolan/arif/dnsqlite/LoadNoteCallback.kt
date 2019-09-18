package com.dolan.arif.dnsqlite

import android.database.Cursor

interface LoadNoteCallback {
    fun preExecute()
    fun postExecute(result: Cursor?)
}