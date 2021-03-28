package com.notes.application.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import com.notes.application.model.DAONotes

class DatabaseHandler(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        private val DATABASE_VERSION = 1
        private val DATABASE_NAME = "NotesDatabase"
        private val TABLE_NOTES = "NotesTable"
        private val KEY_ID = "id"
        private val KEY_DATE = "date"
        private val KEY_NOTES = "notes"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val CREATE_CONTACTS_TABLE = ("CREATE TABLE " + TABLE_NOTES + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_DATE + " TEXT,"
                + KEY_NOTES + " TEXT" + ")")
        db?.execSQL(CREATE_CONTACTS_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTES)
        onCreate(db)
    }

    fun addNotes(notes: DAONotes): Long {
        val db = this.writableDatabase
        val contentValues = ContentValues()
//        contentValues.put(KEY_ID, notes.id)
        contentValues.put(KEY_DATE, notes.date)
        contentValues.put(KEY_NOTES, notes.notes)
        val success = db.insert(TABLE_NOTES, null, contentValues)
        db.close()
        return success
    }


    fun viewNotes(): List<DAONotes> {
        val empList: ArrayList<DAONotes> = ArrayList<DAONotes>()
        val selectQuery = "SELECT  * FROM $TABLE_NOTES"
        val db = this.readableDatabase
        var cursor: Cursor? = null
        try {
            cursor = db.rawQuery(selectQuery, null)
        } catch (e: SQLiteException) {
            db.execSQL(selectQuery)
            return ArrayList()
        }
        var id: Int
        var date: String
        var notes: String
        if (cursor.moveToFirst()) {
            do {
                id = cursor.getInt(cursor.getColumnIndex("id"))
                date = cursor.getString(cursor.getColumnIndex("date"))
                notes = cursor.getString(cursor.getColumnIndex("notes"))
                val emp = DAONotes(id = id, date = date, notes = notes)
                empList.add(emp)
            } while (cursor.moveToNext())
        }
        return empList
    }

    fun deleteEmployee(id: Int): Int {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_ID, id)
        val success = db.delete(TABLE_NOTES, "$KEY_ID=$id", null)
        db.close()
        return success
    }
}  