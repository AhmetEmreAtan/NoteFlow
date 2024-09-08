package com.example.odev7_2

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    lateinit var database: Database
    lateinit var edittxttitle: EditText
    lateinit var edittxtdetail: EditText
    lateinit var btnsave: Button
    lateinit var deleteinfo: ImageButton
    lateinit var recyclerView: RecyclerView
    private lateinit var notesList: MutableList<Notes>
    private lateinit var adapter: NotesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        edittxttitle = findViewById(R.id.edittexttitle)
        edittxtdetail = findViewById(R.id.edittxtdetail)
        btnsave = findViewById(R.id.btnsave)
        deleteinfo = findViewById(R.id.delete_note_info)
        recyclerView = findViewById(R.id.recyclerView)
        database = Database(this)
        notesList = mutableListOf()

        recyclerView.layoutManager = GridLayoutManager(this, 2)

        val verticalSpace = resources.getDimensionPixelSize(R.dimen.vertical_item_spacing)
        val horizontalSpace = resources.getDimensionPixelSize(R.dimen.horizontal_item_spacing)
        recyclerView.addItemDecoration(SpacesItemDecoration(verticalSpace, horizontalSpace))

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
                    Toast.makeText(this@MainActivity, "Successfully Deleted.", Toast.LENGTH_SHORT).show()
                    alertDialog.dismiss()
                }

                alertDialog.show()
            }
        })

        recyclerView.adapter = adapter

        btnsave.setOnClickListener {
            val title = edittxttitle.text.toString()
            val detail = edittxtdetail.text.toString()

            if (title.isEmpty() || detail.isEmpty()) {
                Toast.makeText(this, "You entered incomplete information :)))", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            } else {
                edittxttitle.setText("")
                edittxtdetail.setText("")
            }

            database.insertNote(title, detail)
            showNotes()
        }

        deleteinfo.setOnClickListener {
            Toast.makeText(this, "To delete the note, press and hold the note.", Toast.LENGTH_SHORT).show()
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