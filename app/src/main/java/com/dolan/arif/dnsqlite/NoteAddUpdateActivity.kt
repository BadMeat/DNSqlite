package com.dolan.arif.dnsqlite

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.dolan.arif.dnsqlite.DatabaseContract.Companion.NoteColumn.Companion.CONTENT_URI
import com.dolan.arif.dnsqlite.DatabaseContract.Companion.NoteColumn.Companion.DESC
import com.dolan.arif.dnsqlite.DatabaseContract.Companion.NoteColumn.Companion.TITLE
import kotlinx.android.synthetic.main.activity_note_add_update.*

class NoteAddUpdateActivity : AppCompatActivity(), View.OnClickListener {

    private var note: Note? = null
    //    private lateinit var noteHolper: NoteHelper
    private var position: Int? = null

    private var isEdit = false

    companion object {
        const val EXTRA_POSITION = "extra_position"
        const val EXTRA_NOTE = "extra_note"

        const val REQUEST_ADD = 100
        const val RESULT_ADD = 101

        const val REQUEST_UPDATE = 200
        const val RESULT_UPDATE = 201

        const val RESULT_DELETE = 301

        const val ALERT_DIALOG_CLOSE = 10
        const val ALERT_DIALOG_DELETE = 20

        var CLASS_NAME = NoteAddUpdateActivity::class.java.simpleName
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        if (isEdit) {
            menuInflater.inflate(R.menu.menu_form, menu)
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.action_delete -> {
                showAlertDialog(ALERT_DIALOG_DELETE)
            }
            android.R.id.home -> {
                showAlertDialog(ALERT_DIALOG_CLOSE)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note_add_update)

//        noteHolper = NoteHelper.getInstance(this)

//        note = intent.getParcelableExtra(EXTRA_NOTE)
//        if (note != null) {
//            position = intent.getIntExtra(EXTRA_POSITION, 0)
//            isEdit = true
//        } else {
//            note = Note()
//        }

        val uri = intent.data
        Log.d(CLASS_NAME, "$uri")
        if (uri != null) {
            val cursor = contentResolver.query(uri, null, null, null, null)
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    note = Note(cursor)
                    Log.d(CLASS_NAME, "$note")
                    cursor.close()
                }
            }
        }
        if (note != null) {
            isEdit = true
        }

        val actionBarTitle: String
        val btnTitle: String

        if (isEdit) {
            actionBarTitle = "Ubah"
            btnTitle = "Update"
            if (note != null) {
                edt_title.setText(note?.title)
                edt_desc.setText(note?.desc)
            }
        } else {
            actionBarTitle = "Tambah"
            btnTitle = "Simpan"
        }

        if (supportActionBar != null) {
            supportActionBar?.title = actionBarTitle
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }

        btn_submit.setOnClickListener(this)
        btn_submit.text = btnTitle
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_submit -> {
                val title = edt_title.text.toString()
                val desc = edt_desc.text.toString()

                note?.title = title
                note?.desc = desc

                val intent = Intent()
                intent.putExtra(EXTRA_NOTE, note)
                intent.putExtra(EXTRA_POSITION, position)

                val values = ContentValues()
                values.put(TITLE, title)
                values.put(DESC, desc)

                if (isEdit) {
                    contentResolver.update(this.intent.data!!, values, null, null)
                    Toast.makeText(baseContext, "Item berhasil diedit", Toast.LENGTH_SHORT).show()
                } else {
                    contentResolver.insert(CONTENT_URI, values)
                    Toast.makeText(baseContext, "Item berhasil diambah", Toast.LENGTH_SHORT).show()
                }
                finish()


//                if (isEdit) {
//                    if (note != null) {
//                        val result = noteHolper.updateNote(note!!)?.toLong()
//                        if (result != null) {
//                            if (result > 0) {
//                                setResult(RESULT_UPDATE, intent)
//                                finish()
//                            } else {
//                                Toast.makeText(this, "Gagal Update", Toast.LENGTH_SHORT).show()
//                            }
//                        }
//                    }
//                } else {
//                    val result = noteHolper.insertNote(note!!)
//                    if (result != null) {
//                        if (result > 0) {
//                            note?.id = result.toInt()
//                            setResult(RESULT_ADD, intent)
//                            finish()
//                        } else {
//                            Toast.makeText(this, "Simpan Gagal", Toast.LENGTH_SHORT).show()
//                        }
//                    }
//                }
            }
        }
    }

    private fun showAlertDialog(type: Int) {
        val isDialogClose = type == ALERT_DIALOG_CLOSE
        val dialogTitle: String
        val dialogMessage: String

        if (isDialogClose) {
            dialogTitle = "Batal"
            dialogMessage = "Apakah anda ingin membatalkan perubahan form?"
        } else {
            dialogTitle = "Hapus Note"
            dialogMessage = "Apakah anda ingin Menghapus Data?"
        }

        val alertDialog = AlertDialog.Builder(this)
            .setTitle(dialogTitle)
            .setMessage(dialogMessage)
            .setCancelable(false)
            .setPositiveButton("Ya") { _, _ ->
                if (isDialogClose) {
                    finish()
                } else {
                    val intent = Intent()
                    intent.putExtra(EXTRA_POSITION, position)
                    contentResolver.delete(this.intent.data!!, null, null)
                    Toast.makeText(baseContext, "Item berhasil dihapus", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
            .setNegativeButton("Tidak") { dialog, _ ->
                dialog.cancel()
            }
        val dialog = alertDialog.create()
        dialog.show()
    }

    override fun onBackPressed() {
        showAlertDialog(ALERT_DIALOG_CLOSE)
    }
}
