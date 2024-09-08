package com.example.odev7_2

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

class AddNoteActivity : AppCompatActivity() {

    lateinit var edittxttitle: EditText
    lateinit var edittxtdetail: EditText
    lateinit var priorityButton: Button
    lateinit var btnsave: Button
    lateinit var database: Database



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_note)

        edittxttitle = findViewById(R.id.edittexttitle)
        edittxtdetail = findViewById(R.id.edittxtdetail)
        btnsave = findViewById(R.id.btnsave)
        priorityButton = findViewById(R.id.priority_button)
        database = Database(this)

        btnsave.setOnClickListener {
            val title = edittxttitle.text.toString()
            val detail = edittxtdetail.text.toString()

            if (title.isEmpty() || detail.isEmpty()) {
                Toast.makeText(this, "You entered incomplete information :)))", Toast.LENGTH_SHORT).show()
            } else {
                database.insertNote(title, detail)
                finish()
            }
        }

        priorityButton.setOnClickListener {
            showColorSelectionDialog()
        }
    }

    private fun showColorSelectionDialog() {

        val dialogView = LayoutInflater.from(this).inflate(R.layout.priority_button_alertdialog, null)

        val dialogBuilder = AlertDialog.Builder(this)
            .setView(dialogView)

        val colorListView = dialogView.findViewById<ListView>(R.id.alert_list)
        val cancelButton = dialogView.findViewById<Button>(R.id.alert_button_cancel)

        val colors = listOf("Red", "Green", "Blue", "Yellow")
        val colorValues = listOf(
            ContextCompat.getColor(this, R.color.red),
            ContextCompat.getColor(this, R.color.green),
            ContextCompat.getColor(this, R.color.blue),
            ContextCompat.getColor(this, R.color.yellow)
        )

        val adapter = ColorListAdapter(this, colors, colorValues)
        colorListView.adapter = adapter

        val alertDialog = dialogBuilder.create()
        alertDialog.show()


        colorListView.setOnItemClickListener { _, _, position, _ ->
            priorityButton.backgroundTintList = ColorStateList.valueOf(colorValues[position])
            alertDialog.dismiss()
        }

        cancelButton.setOnClickListener {
            alertDialog.dismiss()
        }
    }

}