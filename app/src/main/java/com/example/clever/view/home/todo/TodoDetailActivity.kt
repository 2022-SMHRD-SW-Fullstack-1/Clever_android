package com.example.clever.view.home.todo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.clever.R
import com.example.clever.databinding.ActivityTodoDetailBinding
import com.example.clever.model.ToDoVo
import com.example.clever.retrofit.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TodoDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTodoDetailBinding

    lateinit var todo_seq: String
    lateinit var cate_seq: String

    lateinit var todo_title: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_todo_detail)

        todo_seq = intent.getStringExtra("todo_seq").toString()
        cate_seq = intent.getStringExtra("cate_seq").toString()

        binding = ActivityTodoDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.todoDetailImgBack.setOnClickListener {
            finish()
        }

        binding.todoDetailBtnCheck.setOnClickListener {
            finish()
        }

        getTodo()

        binding.todoDetailClCpl.setOnClickListener {
            val intent = Intent(this@TodoDetailActivity, TodoCompleteActivity::class.java)
            intent.putExtra("todo_seq", todo_seq)
            intent.putExtra("todo_title", todo_title)
            startActivity(intent)
        }
    }

    private fun getTodo() {
        val req = ToDoVo(todo_seq.toInt(), cate_seq.toInt())
        RetrofitClient.api.getToDo(req).enqueue(object : Callback<ToDoVo> {
            override fun onResponse(call: Call<ToDoVo>, response: Response<ToDoVo>) {
                val res = response.body()!!
                todo_title = res.todo_title.toString()
                binding.todoDetailTvTitle.text = res.todo_title
                binding.todoDetailContent.text = res.todo_content
                binding.todoDetailCategory.text = res.cate_name
                binding.todoDetailRepeat.text = res.todo_repeat
            }

            override fun onFailure(call: Call<ToDoVo>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })
    }
}