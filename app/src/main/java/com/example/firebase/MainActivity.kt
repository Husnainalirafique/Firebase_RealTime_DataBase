package com.example.firebase

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.firebase.databinding.ActivityMainBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var noteAdapter: NoteAdapter
    private lateinit var database: DatabaseReference
    private val notesList: MutableList<Note> = mutableListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        noteAdapter = NoteAdapter()
        database = Firebase.database.reference.child("Notes")

        settingUpRecyclerView()
        addNote()
        fetchNotes()

    }

    private fun addNote() {
        binding.button.setOnClickListener {
            val title = binding.editText.text
            val description = binding.editText2.text
            if (title.toString().isNotEmpty() && description.toString().isNotEmpty()) {
                val notesRef = database.push() // Generate a unique key
                val noteData = mapOf(
                    "title" to title.toString(),
                    "description" to description.toString()
                )
                notesRef.setValue(noteData)
                notesList.add(Note(title.toString(), description.toString()))
                noteAdapter.notifyItemInserted(notesList.size - 1)

                title.clear()
                description.clear()
            } else {
                Toast.makeText(this@MainActivity, "Fill Both fields", Toast.LENGTH_SHORT).show()
            }
        }

    }


    private fun fetchNotes() {
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                notesList.clear()
                for (note in snapshot.children) {
                    val title = note.child("title").getValue(String::class.java)
                    val description = note.child("description").getValue(String::class.java)
                    notesList.add(Note(title, description))
                }
                noteAdapter.submitList(notesList)
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@MainActivity, error.toString(), Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun settingUpRecyclerView() {
        binding.recyclerView.apply {
            layoutManager =
                LinearLayoutManager(this@MainActivity, LinearLayoutManager.VERTICAL, false)
            adapter = noteAdapter
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.unbind()
    }
}


