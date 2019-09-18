package com.dolan.arif.dnsqlite

import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.dolan.arif.dnsqlite.DatabaseContract.Companion.NoteColumn.Companion.CONTENT_URI


class NoteAdapter(private val activity: MainActivity) :
    RecyclerView.Adapter<NoteAdapter.NoteHolder>() {

    private val noteList = mutableListOf<Note>()

    fun getListNote() = noteList

    fun setListNote(listNote: MutableList<Note>) {
        if (noteList.size > 0) {
            noteList.clear()
        }
        noteList.addAll(listNote)
        notifyDataSetChanged()
    }

    fun addNote(note: Note) {
        noteList.add(note)
        notifyItemInserted(noteList.size - 1)
    }

    fun updateNote(position: Int, note: Note) {
        noteList[position] = note
        notifyItemChanged(position)
    }

    fun removeItem(position: Int) {
        noteList.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, noteList.size)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteHolder {
        return NoteHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_note,
                parent,
                false
            )
        )
    }

    override fun getItemCount() = noteList.size

    override
    fun onBindViewHolder(holder: NoteHolder, position: Int) {
        holder.bindItem(noteList[position], position, activity)

    }

    class NoteHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val txtTitle: TextView = view.findViewById(R.id.txt_title)
        private val txtDesc: TextView = view.findViewById(R.id.txt_desc)
        val itemNote: CardView = view.findViewById(R.id.cv_item_note)

        fun bindItem(note: Note, position: Int, activity: MainActivity) {
            txtTitle.text = note.title
            txtDesc.text = note.desc
            itemView.setOnClickListener(
                CustomOnItemClickListener(
                    position,
                    object : CustomOnItemClickListener.OnItemClickCallBack {
                        override fun onItemClicked(view: View?, position: Int) {
                            val uri = Uri.parse("$CONTENT_URI/${note.id}")
                            val intent = Intent(activity, NoteAddUpdateActivity::class.java)
                            Log.d("URINYA", "$uri")
                            intent.data = uri
                            intent.putExtra(NoteAddUpdateActivity.EXTRA_POSITION, position)
                            intent.putExtra(NoteAddUpdateActivity.EXTRA_NOTE, note)
                            activity.startActivityForResult(
                                intent,
                                NoteAddUpdateActivity.REQUEST_UPDATE
                            )

                        }

                    }
                )
            )
        }
    }
}