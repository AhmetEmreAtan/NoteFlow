package com.example.odev7_2

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class Database(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        const val DATABASE_VERSION = 1
        const val DATABASE_NAME = "notes.db"
        const val TABLE_NAME = "notes"
        const val KEY_ID = "id"
        const val KEY_TITLE  = "title"
        const val KEY_DETAIL  = "detail"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTable =
            "CREATE TABLE $TABLE_NAME ($KEY_ID INTEGER PRIMARY KEY AUTOINCREMENT, $KEY_TITLE  TEXT, $KEY_DETAIL  TEXT)"
        db?.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun insertNote(title: String, detail: String): Long {
        val values = ContentValues()
        values.put(KEY_TITLE, title)
        values.put(KEY_DETAIL , detail)
        val db = this.writableDatabase
        val result = db.insert(TABLE_NAME, null, values)
        db.close()
        return result
    }

    fun getAllNotes(): List<Notes> {
        val notesList = ArrayList<Notes>()
        val selectQuery = "SELECT  * FROM $TABLE_NAME ORDER BY $KEY_ID DESC"
        val db = this.readableDatabase
        val cursor = db.rawQuery(selectQuery, null)
        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndex(KEY_ID))
                val title = cursor.getString(cursor.getColumnIndex(KEY_TITLE))
                val detail = cursor.getString(cursor.getColumnIndex(KEY_DETAIL))
                val note = Notes(id, title, detail)
                notesList.add(note)
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return notesList
    }

    fun deleteNoteById(id: Int): Int {
        val db = this.writableDatabase
        val result = db.delete(TABLE_NAME, "$KEY_ID = ?", arrayOf(id.toString()))
        db.close()
        return result
    }

    fun updateNote(id: Int, title: String, detail: String): Int {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(KEY_TITLE, title)
            put(KEY_DETAIL, detail)
        }
        val result = db.update(TABLE_NAME, values, "$KEY_ID = ?", arrayOf(id.toString()))
        db.close()
        return result
    }

    fun getNoteById(id: Int): Notes? {
        val db = this.readableDatabase
        val cursor = db.query(
            TABLE_NAME,
            arrayOf(KEY_ID, KEY_TITLE, KEY_DETAIL),
            "$KEY_ID = ?",
            arrayOf(id.toString()),
            null, null, null
        )
        var note: Notes? = null
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                val noteId = cursor.getInt(cursor.getColumnIndex(KEY_ID))
                val title = cursor.getString(cursor.getColumnIndex(KEY_TITLE))
                val detail = cursor.getString(cursor.getColumnIndex(KEY_DETAIL))
                note = Notes(noteId, title, detail)
            }
            cursor.close()
        }
        db.close()
        return note
    }
}