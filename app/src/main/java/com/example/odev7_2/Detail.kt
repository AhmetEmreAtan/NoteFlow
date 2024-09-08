package com.example.odev7_2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast

class Detail : AppCompatActivity() {

    lateinit var titleEditText: EditText
    lateinit var detailEditText: EditText
    lateinit var saveChangesImageButton: ImageButton
    lateinit var db: Database
    var noteId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        titleEditText = findViewById(R.id.txttitle)
        detailEditText = findViewById(R.id.txtdetail)
        saveChangesImageButton = findViewById(R.id.saveChangesImageButton)
        db = Database(this)

        noteId = intent.getIntExtra("NOTE_ID", -1)
        if (noteId != -1) {
            val note = db.getNoteById(noteId)
            note?.let {
                titleEditText.setText(it.title)
                detailEditText.setText(it.detail)
            }
        }

        // Değişiklikleri kaydetme işlemi
        saveChangesImageButton.setOnClickListener {
            val updatedTitle = titleEditText.text.toString()
            val updatedDetail = detailEditText.text.toString()

            if (updatedTitle.isEmpty() || updatedDetail.isEmpty()) {
                Toast.makeText(this, "Title or detail cannot be empty.", Toast.LENGTH_SHORT).show()
            } else {
                db.updateNote(noteId, updatedTitle, updatedDetail)
                Toast.makeText(this, "Note updated successfully.", Toast.LENGTH_SHORT).show()
                finish() // Detay sayfasından çıkış
            }
        }
    }
}