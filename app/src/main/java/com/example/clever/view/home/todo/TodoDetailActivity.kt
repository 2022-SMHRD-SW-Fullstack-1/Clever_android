package com.example.clever.view.home.todo

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
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
    private lateinit var memId: String

    lateinit var cmpl_seq: String

    var todoModify = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_todo_detail)

        todo_seq = intent.getStringExtra("todo_seq").toString()
        cate_seq = intent.getStringExtra("cate_seq").toString()
        cmpl_seq = intent.getStringExtra("cmpl_seq").toString()

        if (intent.getStringExtra("cmpl_seq") == null) {
            getTodo()
        } else {
            getCmpl()
            todoModify = true
        }

        loginSp = getSharedPreferences("loginInfo", Context.MODE_PRIVATE)
        memId = loginSp.getString("mem_id", "").toString()

        binding = ActivityTodoDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.todoDetailImgBack.setOnClickListener {
            finish()
        }

        binding.todoDetailBtnCheck.setOnClickListener {
            if (!todoModify) {
                todoCmpl()
            } else {
                todoModify()
            }
        }

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
                if (res.mem_name.toString() == "" || res.mem_name.toString() == "null" || res.mem_name.toString() == null) {
                    binding.todoTvChargePerson.visibility = View.GONE
                } else {
                    binding.todoTvChargePerson.visibility = View.VISIBLE
                    binding.todoTvChargePerson.text = "${res.mem_name.toString()} 담당"
                }
            }

            override fun onFailure(call: Call<ToDoVo>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })
    }

    private fun getCmpl() {
        RetrofitClient.api.getCmpl(cmpl_seq.toInt()).enqueue(object : Callback<ToDoCompleteVO> {
            override fun onResponse(
                call: Call<ToDoCompleteVO>,
                response: Response<ToDoCompleteVO>
            ) {
                binding.textView33.text = "완료"
                val res = response.body()
                binding.todoDetailContent.text = res!!.todo_content.toString()
                binding.todoDetailCb.isChecked = res.cmpl_strange == "Y"
                binding.todoDetailEtMemo.setText(res.cmpl_memo)
            }

            override fun onFailure(call: Call<ToDoCompleteVO>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })
    }

    private fun todoCmpl() {
        val memo = binding.todoDetailEtMemo.text.toString()
        var strange: String = if (binding.todoDetailCb.isChecked) {
            "Y"
        } else {
            "N"
        }
        if (strange == "Y" && memo == "") {
            Toast.makeText(this@TodoDetailActivity, "메모를 입력해 주세요 !", Toast.LENGTH_SHORT).show()
        } else {
            val req = ToDoCompleteVO(todo_seq.toInt(), memId, "", memo, strange, cate_seq.toInt())
            RetrofitClient.api.todoCmpl(req).enqueue(object : Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    val res = response.body()?.string()
                    if (res.toString() == "1") {
                        finish()
                    } else {
                        Toast.makeText(this@TodoDetailActivity, "완료에 실패했습니다.", Toast.LENGTH_SHORT)
                            .show()
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    TODO("Not yet implemented")
                }
            })
        }
    }

    private fun todoModify() {
        val memo = binding.todoDetailEtMemo.text.toString()
        var strange: String = if (binding.todoDetailCb.isChecked) {
            "Y"
        } else {
            "N"
        }
        val req = ToDoCompleteVO(
            cmpl_seq.toInt(),
            todo_seq.toInt(),
            memId,
            null,
            "",
            memo,
            strange,
            cate_seq.toInt(),
            null,
            null,
            null
        )
        RetrofitClient.api.todoModify(req).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                val res = response.body()?.string()
                if (res.toString() == "1") {
                    finish()
                } else {
                    Toast.makeText(this@TodoDetailActivity, "수정에 실패했습니다.", Toast.LENGTH_SHORT)
                        .show()
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })
    }
}