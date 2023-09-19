package com.example.firebase.note

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.firebase.R
import com.example.firebase.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var noteAdapter: NoteAdapter
    private lateinit var noteViewModel: NoteViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        noteAdapter = NoteAdapter()
        noteViewModel = ViewModelProvider(this)[NoteViewModel::class.java]

        setupRecyclerView()
        addNotes()
        observeNotes()
        deleteAllNotes()
    }

    private fun addNotes() {
        binding.button.setOnClickListener {
            val title = binding.editText.text.toString()
            val description = binding.editText2.text.toString()
            if (title.isNotEmpty() && description.isNotEmpty()) {
                noteViewModel.addNewNote(title, description)
                binding.editText.text.clear()
                binding.editText2.text.clear()
            } else {
                Toast.makeText(this@MainActivity, "Fill both!", Toast.LENGTH_SHORT).show()
            }
        }
    }


    private fun deleteAllNotes(){
        binding.buttonDelete.setOnClickListener {
            noteViewModel.deleteAllNotes()
        }
    }

    private fun observeNotes() {
        noteViewModel.getNotesList().observe(this) { notes ->
            notes?.let {
                noteAdapter.submitList(notes)
            }
        }
    }

    private fun setupRecyclerView() {
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




