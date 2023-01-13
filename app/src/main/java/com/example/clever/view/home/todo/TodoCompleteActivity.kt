package com.example.clever.view.home.todo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.clever.R
import com.example.clever.databinding.ActivityTodoCompleteBinding

class TodoCompleteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTodoCompleteBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_todo_complete)

        binding = ActivityTodoCompleteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.todoCplImgBack.setOnClickListener {
            finish()
        }

    }
}