package com.example.clever.view.home.todo

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.clever.R
import com.example.clever.databinding.ActivityTodoDetailBinding
import com.example.clever.model.ToDoCompleteVO
import com.example.clever.model.ToDoVo
import com.example.clever.retrofit.RetrofitClient
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TodoDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTodoDetailBinding

    lateinit var todo_seq: String
    lateinit var cate_seq: String

    lateinit var todo_title: String

    lateinit var loginSp: SharedPreferences
    private lateinit var memId:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_todo_detail)

        todo_seq = intent.getStringExtra("todo_seq").toString()
        cate_seq = intent.getStringExtra("cate_seq").toString()

        loginSp = getSharedPreferences("loginInfo", Context.MODE_PRIVATE)
        memId = loginSp.getString("mem_id", "").toString()

        binding = ActivityTodoDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.todoDetailImgBack.setOnClickListener {
            finish()
        }

        binding.todoDetailBtnCheck.setOnClickListener {
            todoCmpl()
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
                Log.d("gettodo", res.toString())
                todo_title = res.todo_title.toString()
                binding.todoDetailTvTitle.text = res.todo_title
                binding.todoDetailContent.text = res.todo_content
            }

            override fun onFailure(call: Call<ToDoVo>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })
    }

    private fun todoCmpl(){
        val memo = binding.todoDetailEtMemo.text.toString()
        var strange: String
        if(binding.todoDetailCb.isChecked){
            strange = "Y"
        }else{
            strange = "N"
        }
        val req = ToDoCompleteVO(todo_seq.toInt(), memId,"", memo, strange, cate_seq.toInt())
        RetrofitClient.api.todoCmpl(req).enqueue(object : Callback<ResponseBody>{
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                val res = response.body()?.string()
                if(res.toString() == "1"){
                    finish()
                }else{
                    Toast.makeText(this@TodoDetailActivity, "완료에 실패했습니다.", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })
    }
}