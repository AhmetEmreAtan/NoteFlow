package com.example.odev7_2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    lateinit var database: Database
    lateinit var recyclerView: RecyclerView
    lateinit var addNoteButton: FloatingActionButton
    lateinit var toastButton: ImageButton
    private lateinit var notesList: MutableList<Notes>
    private lateinit var adapter: NotesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recyclerView)
        addNoteButton = findViewById(R.id.add_note)
        toastButton = findViewById(R.id.delete_note_info)
        database = Database(this)
        notesList = mutableListOf()

        recyclerView.layoutManager = GridLayoutManager(this, 1)

        val verticalSpace = resources.getDimensionPixelSize(R.dimen.vertical_item_spacing)
        val horizontalSpace = resources.getDimensionPixelSize(R.dimen.horizontal_item_spacing)
        recyclerView.addItemDecoration(SpacesItemDecoration(verticalSpace, horizontalSpace))

        toastButton.setOnClickListener {
            Toast.makeText(this, "Long press to delete the note.", Toast.LENGTH_SHORT).show()
        }

        adapter = NotesAdapter(this, notesList, object : NotesAdapter.OnItemClickListener {
            override fun onItemClick(note: Notes, position: Int) {

                val intent = Intent(this@MainActivity, Detail::class.java)
                intent.putExtra("NOTE_ID", note.id)
                startActivity(intent)
            }
        }, object : NotesAdapter.OnItemLongClickListener {
            override fun onItemLongClick(note: Notes, position: Int) {
                val dialogView = LayoutInflater.from(this@MainActivity).inflate(R.layout.custom_alert_dialog, null)
                val dialogBuilder = AlertDialog.Builder(this@MainActivity).setView(dialogView)

                val alertDialog = dialogBuilder.create()

                val btnCancel = dialogView.findViewById<Button>(R.id.btn_cancel)
                val btnConfirm = dialogView.findViewById<Button>(R.id.btn_confirm)

                btnCancel.setOnClickListener {
                    alertDialog.dismiss()
                }

                btnConfirm.setOnClickListener {
                    database.deleteNoteById(note.id)
                    notesList.removeAt(position)
                    adapter.notifyDataSetChanged()
                    Toast.makeText(this@MainActivity, "Note deleted successfully.", Toast.LENGTH_SHORT).show()
                    alertDialog.dismiss()
                }

                alertDialog.show()
            }
        })

        recyclerView.adapter = adapter

        addNoteButton.setOnClickListener {
            val intent = Intent(this@MainActivity, AddNoteActivity::class.java)
            startActivity(intent)
        }

        showNotes()
    }


    override fun onResume() {
        super.onResume()
        showNotes()
    }


    private fun showNotes() {
        val notes = database.getAllNotes()
        notesList.clear()
        notesList.addAll(notes)
        adapter.notifyDataSetChanged()
    }
}

