package com.example.firebase

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.firebase.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var noteAdapter: NoteAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        setOnClickListener()
        noteAdapter = NoteAdapter()
        settingUpRecyclerView()

    }

    private fun setOnClickListener() {

    }

    private fun settingUpRecyclerView() {
        val note = listOf(
            Note("a","Note"),
            Note("b","Note2")
        )
        noteAdapter.submitList(note)
            binding.recyclerView.apply {
                layoutManager = LinearLayoutManager(this@MainActivity,LinearLayoutManager.VERTICAL,false)
                adapter = noteAdapter

            }
        }

    override fun onDestroy() {
        super.onDestroy()
        binding.unbind()
    }
}