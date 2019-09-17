package com.dolan.arif.dnsqlite

import android.content.Context
import android.content.Intent
import android.database.ContentObserver
import android.database.Cursor
import android.os.*
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.dolan.arif.dnsqlite.DatabaseContract.Companion.NoteColumn.Companion.CONTENT_URI
import com.dolan.arif.dnsqlite.NoteAddUpdateActivity.Companion.EXTRA_NOTE
import com.dolan.arif.dnsqlite.NoteAddUpdateActivity.Companion.EXTRA_POSITION
import com.dolan.arif.dnsqlite.NoteAddUpdateActivity.Companion.REQUEST_ADD
import com.dolan.arif.dnsqlite.NoteAddUpdateActivity.Companion.REQUEST_UPDATE
import com.dolan.arif.dnsqlite.NoteAddUpdateActivity.Companion.RESULT_ADD
import com.dolan.arif.dnsqlite.NoteAddUpdateActivity.Companion.RESULT_DELETE
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.ref.WeakReference

class MainActivity : AppCompatActivity(), View.OnClickListener, LoadNoteCallback {

    private lateinit var noteAdapter: NoteAdapter
    //    private lateinit var noteHelper: NoteHelper
    private lateinit var handlerThread: HandlerThread
    private lateinit var myObserver: DataObserver

    companion object {
        const val EXTRA_STATE = "EXTRA_STATE"
    }


    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.fab_add -> {
                val intent = Intent(this, NoteAddUpdateActivity::class.java)
                startActivityForResult(intent, REQUEST_ADD)
            }
        }
    }

    override fun preExecute() {
        runOnUiThread {
            progressbar.visibility = View.VISIBLE
        }
    }

    override fun postExecute(note: List<Note>?) {
        progressbar.visibility = View.INVISIBLE
        if (note != null) {
            noteAdapter.setListNote(note.toMutableList())
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        rv_notes.layoutManager = LinearLayoutManager(this)
        rv_notes.setHasFixedSize(true)

//        noteHelper = NoteHelper.getInstance(this)
//
//        noteHelper.open()

        handlerThread = HandlerThread("DataObserver")
        handlerThread.start()
        val handler = Handler(handlerThread.looper)
        myObserver = DataObserver(handler, this)
        contentResolver.registerContentObserver(CONTENT_URI, true, myObserver)

        fab_add.setOnClickListener(this)

        noteAdapter = NoteAdapter(this)
        rv_notes.adapter = noteAdapter

        if (savedInstanceState == null) {
            LoadNoteAsync(this, this).execute()
        } else {
            val list: ArrayList<Note>? = savedInstanceState.getParcelableArrayList(EXTRA_STATE)
            if (list != null) {
                noteAdapter.setListNote(list)
            }
        }
    }

    private class LoadNoteAsync(
        noteHelper: Context,
        callBack: LoadNoteCallback
    ) : AsyncTask<Void, Void, Cursor> {

        private val weakContext: WeakReference<Context> = WeakReference(noteHelper)
        private val weakCallBack: WeakReference<LoadNoteCallback> = WeakReference(callBack)

        override fun onPreExecute() {
            super.onPreExecute()
            weakCallBack.get()?.preExecute()
        }

        override fun doInBackground(vararg params: Void?): Cursor {
            return weakContext.get()!!.getAllNotes()
        }

        override fun onPostExecute(result: Cursor) {
            super.onPostExecute(result)
            weakCallBack.get()?.postExecute(result)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList(
            EXTRA_STATE,
            noteAdapter.getListNote() as java.util.ArrayList<out Parcelable>
        )
        startActivityForResult(intent, REQUEST_ADD)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (data != null) {
            if (requestCode == REQUEST_ADD) {
                if (resultCode == RESULT_ADD) {
                    val note = data.getParcelableExtra<Note>(EXTRA_NOTE)
                    noteAdapter.addNote(note)
                    rv_notes.smoothScrollToPosition(noteAdapter.itemCount - 1)
                    Toast.makeText(this, "Item Berhasil Ditambahkan", Toast.LENGTH_SHORT).show()
                }
            } else if (requestCode == REQUEST_UPDATE) {
                if (resultCode == NoteAddUpdateActivity.RESULT_UPDATE) {
                    val note = data.getParcelableExtra<Note>(EXTRA_NOTE)
                    val position = data.getIntExtra(EXTRA_POSITION, 0)
                    noteAdapter.updateNote(position, note)
                    rv_notes.smoothScrollToPosition(position)
                    Toast.makeText(this, "Data Berhasil Diupdate", Toast.LENGTH_SHORT).show()
                } else if (resultCode == RESULT_DELETE) {
                    val position = data.getIntExtra(EXTRA_POSITION, 0)
                    noteAdapter.removeItem(position)
                    Toast.makeText(this, "Data Berhasil Dihapus", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        noteHelper.close()
    }

    class DataObserver(handler: Handler, ctx: Context) : ContentObserver(handler) {

        private var context: Context = ctx

        override fun onChange(selfChange: Boolean) {
            super.onChange(selfChange)
            LoadNoteAsync(context, context as LoadNoteCallback).execute()
        }
    }
}
