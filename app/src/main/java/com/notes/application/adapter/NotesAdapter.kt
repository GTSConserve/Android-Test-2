package com.notes.application.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.notes.application.model.DAONotes
import com.notes.application.MainActivity
import com.notes.application.R

class NotesAdapter(
    var context: MainActivity,
    var mNotes: List<DAONotes>
) :
    RecyclerView.Adapter<NotesAdapter.NotesViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotesViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.adapter_notes, parent, false)
        return NotesViewHolder(v)
    }

    override fun getItemCount(): Int {
        return mNotes.size
    }

    override fun onBindViewHolder(holder: NotesViewHolder, position: Int) {
        holder.tvDate.text = mNotes[position].date
        holder.tvNotes.text = mNotes[position].notes
        holder.ivDelete.setOnClickListener {
            context.deleteNotes(mNotes[position].id)
        }
    }

    class NotesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvNotes = itemView.findViewById<TextView>(R.id.tvNotes)
        var tvDate = itemView.findViewById<TextView>(R.id.tvDate)
        var ivDelete = itemView.findViewById<ImageView>(R.id.ivDelete)
    }
}