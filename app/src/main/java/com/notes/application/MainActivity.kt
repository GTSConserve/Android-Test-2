package com.notes.application

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.Window
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.notes.application.adapter.NotesAdapter
import com.notes.application.database.DatabaseHandler
import com.notes.application.model.DAONotes
import kotlinx.android.synthetic.main.activity_main.*
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))
        viewNotes()
        fab.setOnClickListener {
            showAddNotesDialog(this)
        }
    }

    private fun showAddNotesDialog(mContext: Activity) {
        val dialog = Dialog(mContext)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.dialog_add_notes)
        dialog.window!!.setLayout(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        val etNotes = dialog.findViewById<EditText>(R.id.etNotes)
        val dialogCancel = dialog.findViewById<TextView>(R.id.tvCancel)
        val dialogSave = dialog.findViewById<TextView>(R.id.tvSave)
        dialogCancel.setOnClickListener {
            dialog.dismiss()
        }

        dialogSave.setOnClickListener {
            if (etNotes.text.toString().isEmpty()) {
                Toast.makeText(this, "Enter Notes", Toast.LENGTH_SHORT).show()
            } else {
                val date = SimpleDateFormat("MMM d").format(Date())
                DatabaseHandler(this).addNotes(DAONotes(0, date, etNotes.text.toString()))
                viewNotes()
                dialog.dismiss()
            }
        }

        dialog.show()
    }

    private fun viewNotes() {
        val databaseHandler = DatabaseHandler(this)
        rvNotes.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rvNotes.adapter = NotesAdapter(this, databaseHandler.viewNotes())
    }

    fun deleteNotes(notesid: Int) {
        val builder = AlertDialog.Builder(this)
        builder.setMessage("Are you sure want to delete the notes?").setCancelable(false)
            .setPositiveButton("Yes") { dialog, id ->
                val status = DatabaseHandler(this).deleteEmployee(notesid)
                if (status > -1) {
                    viewNotes()
                }
                dialog.dismiss()
            }.setNegativeButton("No") { dialog, id -> dialog.dismiss() }
        builder.show()
    }
}