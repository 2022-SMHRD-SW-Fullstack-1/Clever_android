package com.example.clever.view.home.todo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.GridLayoutManager
import com.example.clever.R
import com.example.clever.adapter.ToDoCompleteAdapter
import com.example.clever.databinding.ActivityTodoCompleteBinding
import com.example.clever.decorator.other.TodoTab2Decorator
import com.example.clever.model.ToDoCompleteVO
import com.example.clever.retrofit.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TodoCompleteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTodoCompleteBinding

    lateinit var todo_seq: String
    lateinit var todo_title: String

    //item
    val todoCmplList = ArrayList<ToDoCompleteVO>()

    // adapter
    lateinit var adapter: ToDoCompleteAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_todo_complete)

        todo_seq = intent.getStringExtra("todo_seq").toString()
        todo_title = intent.getStringExtra("todo_title").toString()

        binding = ActivityTodoCompleteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.todoCplTvTitle.text = todo_title

        // container
        // todoCplRv

        // template
        // template_todo_cpl_rv

        // item
        getToDoCmplList()

        // adapter
        adapter = ToDoCompleteAdapter(
            this@TodoCompleteActivity,
            todoCmplList
        )

        // adapter, container 연결
        binding.todoCplRv.adapter = adapter
        binding.todoCplRv.layoutManager = GridLayoutManager(this@TodoCompleteActivity, 1)
        binding.todoCplRv.addItemDecoration(TodoTab2Decorator(16))

        // event

        binding.todoCplImgBack.setOnClickListener {
            finish()
        }
    }

    private fun getToDoCmplList() {
        val req = ToDoCompleteVO(todo_seq.toInt(), 0, null)
        RetrofitClient.api.getToDoCmplList(req).enqueue(object : Callback<List<ToDoCompleteVO>>{
            override fun onResponse(
                call: Call<List<ToDoCompleteVO>>,
                response: Response<List<ToDoCompleteVO>>
            ) {
                val res = response.body()
                for(i in 0 until res!!.size){
                    todoCmplList.add(res[i])
                    Log.d("완료", todoCmplList[i].toString())
                }
                adapter.notifyDataSetChanged()
            }

            override fun onFailure(call: Call<List<ToDoCompleteVO>>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })

    }
}