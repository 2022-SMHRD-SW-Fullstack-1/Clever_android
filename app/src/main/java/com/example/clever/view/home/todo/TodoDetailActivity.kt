package com.example.clever.view.home.todo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.clever.R
import com.example.clever.databinding.ActivityTodoDetailBinding

class TodoDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTodoDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_todo_detail)

        binding = ActivityTodoDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.todoDetailImgBack.setOnClickListener {
            finish()
        }

        binding.todoDetailBtnCheck.setOnClickListener {
            finish()
        }

        binding.todoDetailClCpl.setOnClickListener {
            val intent = Intent(this@TodoDetailActivity, TodoCompleteActivity::class.java)
            startActivity(intent)
        }
    }
}