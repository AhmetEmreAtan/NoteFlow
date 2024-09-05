package com.example.odev7_2

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class NotesAdapter(
    private val context: Context,
    private val notes: List<Notes>,
    private val itemClickListener: OnItemClickListener,
    private val itemLongClickListener: OnItemLongClickListener
) : RecyclerView.Adapter<NotesAdapter.NoteViewHolder>() {

    inner class NoteViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val titleTextView: TextView = view.findViewById(R.id.txtTitle)
        val detailTextView: TextView = view.findViewById(R.id.txtDetail)

        fun bind(note: Notes, clickListener: OnItemClickListener, longClickListener: OnItemLongClickListener) {
            titleTextView.text = note.title
            detailTextView.text = note.detail


            itemView.setOnClickListener {
                clickListener.onItemClick(note, adapterPosition)
            }


            itemView.setOnLongClickListener {
                longClickListener.onItemLongClick(note, adapterPosition)
                true
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.list_item_note, parent, false)
        return NoteViewHolder(view)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val note = notes[position]
        holder.bind(note, itemClickListener, itemLongClickListener)
    }

    override fun getItemCount(): Int = notes.size

    interface OnItemClickListener {
        fun onItemClick(note: Notes, position: Int)
    }

    interface OnItemLongClickListener {
        fun onItemLongClick(note: Notes, position: Int)
    }
}