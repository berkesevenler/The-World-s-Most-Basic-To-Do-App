package com.example.simpletodoapp

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.simpletodoapp.databinding.ActivityMainBinding
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val taskList = mutableListOf<Task>()
    private val adapter = TaskAdapter(taskList)
    private val sharedPreferences by lazy {
        getSharedPreferences("SimpleToDoApp", Context.MODE_PRIVATE)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize View Binding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Load saved tasks
        loadTasks()

        // Setup RecyclerView
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(this)

        // Set Add Task Button Click Listener
        binding.addTaskButton.setOnClickListener {
            val taskName = binding.taskInput.text.toString()
            if (taskName.isNotEmpty()) {
                taskList.add(Task(taskName))
                adapter.notifyItemInserted(taskList.size - 1)
                binding.taskInput.text.clear()

                // Save tasks to SharedPreferences
                saveTasks()
            }
        }
    }

    private fun saveTasks() {
        val gson = Gson()
        val json = gson.toJson(taskList)
        sharedPreferences.edit().putString("tasks", json).apply()
    }

    private fun loadTasks() {
        val gson = Gson()
        val json = sharedPreferences.getString("tasks", null)
        if (!json.isNullOrEmpty()) {
            val type = object : TypeToken<MutableList<Task>>() {}.type
            val savedTasks: MutableList<Task> = gson.fromJson(json, type)
            taskList.addAll(savedTasks)
        }
    }
}
