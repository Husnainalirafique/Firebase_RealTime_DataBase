package com.example.firebase

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.firebase.databinding.ActivityMainBinding
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ConfigUpdate
import com.google.firebase.remoteconfig.ConfigUpdateListener
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigException
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var noteAdapter: NoteAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        noteAdapter = NoteAdapter()
        setOnClickListener()
        settingUpRecyclerView()
        firebaseRemoteConfig()

    }

    private fun firebaseRemoteConfig() {
        val remoteConfig = Firebase.remoteConfig
        remoteConfig.apply {
            setConfigSettingsAsync(remoteConfigSettings { minimumFetchIntervalInSeconds =  3600})
            setDefaultsAsync(R.xml.remote_config_defaults)
            fetchAndActivate().addOnCompleteListener(this@MainActivity) { task ->
                if (task.isSuccessful) {
                    val addNoteBtnText = remoteConfig.getString("ADD_NOTE_KEY")
                    binding.button.text = addNoteBtnText
                }
            }
        }
        remoteConfig.addOnConfigUpdateListener(object : ConfigUpdateListener {
            override fun onUpdate(configUpdate : ConfigUpdate) {
                if (configUpdate.updatedKeys.contains("ADD_NOTE_KEY")) {
                    remoteConfig.activate().addOnCompleteListener {
                        binding.button.text = remoteConfig.getString("ADD_NOTE_KEY")
                    }
                }
            }
            override fun onError(error : FirebaseRemoteConfigException) {
                Toast.makeText(this@MainActivity, "error", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun setOnClickListener() {

    }

    private fun settingUpRecyclerView() {
        val note = listOf(
            Note("a", "Note"),
            Note("b", "Note2")
        )
        noteAdapter.submitList(note)
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