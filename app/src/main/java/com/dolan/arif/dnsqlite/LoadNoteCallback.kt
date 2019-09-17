package com.dolan.arif.dnsqlite

interface LoadNoteCallback {
    fun preExecute()
    fun postExecute(note: List<Note>?)
}