package com.example.clever.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.clever.R
import com.example.clever.model.CategoryVO
import com.example.clever.model.ToDoCompleteVO
import com.example.clever.model.ToDoVo
import com.example.clever.retrofit.RetrofitClient
import com.example.clever.utils.Time
import com.example.clever.view.home.todo.TodoListActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TodoFragmentAdapter(val context: Context, val categoryList: ArrayList<CategoryVO>) :
    RecyclerView.Adapter<TodoFragmentAdapter.ViewHolder>() {
    lateinit var loginSp: SharedPreferences
    private lateinit var memId: String
    lateinit var groupSp: SharedPreferences
    lateinit var group_seq: String

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val todoCategoryTvTitle: TextView
        val todoCategoryTvState: TextView
        val todoCategoryCl: ConstraintLayout

        init {
            todoCategoryTvTitle = itemView.findViewById(R.id.todoCategoryTvTitle)
            todoCategoryTvState = itemView.findViewById(R.id.todoCategoryTvState)
            todoCategoryCl = itemView.findViewById(R.id.todoCategoryCl)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(context)
        val view = layoutInflater.inflate(R.layout.template_todo_category_rv, null)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, @SuppressLint("RecyclerView") position: Int) {
        holder.todoCategoryTvTitle.text = categoryList[position].cate_name

        holder.todoCategoryCl.setOnClickListener {
            val intent = Intent(context, TodoListActivity::class.java)
            intent.putExtra("cate_seq", "${categoryList[position].cate_seq}")
            intent.putExtra("cate_name", categoryList[position].cate_name)
            intent.putExtra("cate_type", categoryList[position].cate_type)
            context.startActivity(intent)
        }

        var all: Int
        var cpl: Int
        if (categoryList[position].cate_type != "Default") {
            val reqTodo = ToDoVo(categoryList[position].cate_seq!!)
            RetrofitClient.api.getToDoList(reqTodo).enqueue(object : Callback<List<ToDoVo>> {
                override fun onResponse(
                    call: Call<List<ToDoVo>>,
                    response: Response<List<ToDoVo>>
                ) {
                    val res = response.body()
                    all = res!!.size

                    val reqCmpl = ToDoCompleteVO(categoryList[position].cate_seq!!)
                    RetrofitClient.api.getToDoComplete(reqCmpl)
                        .enqueue(object : Callback<List<ToDoCompleteVO>> {
                            override fun onResponse(
                                call: Call<List<ToDoCompleteVO>>,
                                response: Response<List<ToDoCompleteVO>>
                            ) {
                                val res = response.body()
                                val resArr = ArrayList<ToDoCompleteVO>()
                                for (i in 0 until res!!.size) {
                                    var date = res[i].cmpl_time!!
                                    val dateY = date.substring(0, 4)
                                    var dateM = date.substring(5, 7)
                                    var dateD = date.substring(8, 10)
                                    date = "${dateY}-${dateM}-${dateD}"
                                    var today = Time.getTime()
                                    if (date == today) {
                                        resArr.add(res[i])
                                    }
                                }
                                cpl = resArr.size
                                holder.todoCategoryTvState.text = "${cpl}/${all} 완료"
                            }

                            override fun onFailure(call: Call<List<ToDoCompleteVO>>, t: Throwable) {
                                TODO("Not yet implemented")
                            }
                        })
                }

                override fun onFailure(call: Call<List<ToDoVo>>, t: Throwable) {
                    TODO("Not yet implemented")
                }
            })
        } else {
            loginSp = context.getSharedPreferences("loginInfo", Context.MODE_PRIVATE)
            memId = loginSp.getString("mem_id", "").toString()
            groupSp = context.getSharedPreferences("groupInfo", Context.MODE_PRIVATE)
            group_seq = groupSp.getString("group_seq", "").toString()
            RetrofitClient.api.getMyToDo(group_seq.toInt(), memId)
                .enqueue(object : Callback<List<ToDoVo>> {
                    override fun onResponse(
                        call: Call<List<ToDoVo>>,
                        response: Response<List<ToDoVo>>
                    ) {
                        val res = response.body()
                        all = res!!.size

                        RetrofitClient.api.getMyToDoComplete(
                            group_seq.toInt(),
                            memId,
                            Time.getTime()
                        ).enqueue(object : Callback<List<ToDoCompleteVO>> {
                            override fun onResponse(
                                call: Call<List<ToDoCompleteVO>>,
                                response: Response<List<ToDoCompleteVO>>
                            ) {
                                val res = response.body()
                                cpl = res!!.size
                                holder.todoCategoryTvState.text = "${cpl}/${all} 완료"
                            }

                            override fun onFailure(call: Call<List<ToDoCompleteVO>>, t: Throwable) {
                                TODO("Not yet implemented")
                            }
                        })
                    }

                    override fun onFailure(call: Call<List<ToDoVo>>, t: Throwable) {
                        TODO("Not yet implemented")
                    }
                })
        }
    }

    override fun getItemCount(): Int {
        return categoryList.size
    }
}