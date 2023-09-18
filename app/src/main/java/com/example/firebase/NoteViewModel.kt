package com.example.firebase
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.database.ktx.database

class NoteViewModel : ViewModel() {
    private val database: DatabaseReference = Firebase.database.reference
    private val notesListLiveData: MutableLiveData<List<Note>> = MutableLiveData()

    init {
        fetchNotes()
    }

    fun getNotesList(): LiveData<List<Note>> {
        return notesListLiveData
    }

    fun addNewNote(title: String, description: String) {
        val note = Note(title, description)
        database.child("Notes").push().setValue(note)
    }

    private fun fetchNotes() {
        val dbRef = database.child("Notes")
        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val notesList: MutableList<Note> = mutableListOf()
                for (note in snapshot.children) {
                    val title = note.child("noteTitle").getValue(String::class.java)
                    val description = note.child("noteDescription").getValue(String::class.java)
                    notesList.add(Note(title, description))
                }
                notesListLiveData.value = notesList
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle errors here
            }
        })
    }
}
