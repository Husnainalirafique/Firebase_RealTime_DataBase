package com.example.firebase.note
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.database.ktx.database

class NoteViewModel : ViewModel() {
    private val database = Firebase.database.reference
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

    fun deleteAllNotes(){
        database.child("Notes").removeValue()
    }

    private fun fetchNotes() {
        val dbRef = database.child("Notes")
        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val notesList: MutableList<Note> = mutableListOf()
                for (noteSnap in snapshot.children) {
                    val noteId = noteSnap.key
                    val note = noteSnap.getValue(Note::class.java)
                    note?.let {
                        notesList.add(Note(it.noteTitle,it.noteDescription))
                    }
                }
                notesListLiveData.value = notesList
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle errors here
            }
        })
    }


}
