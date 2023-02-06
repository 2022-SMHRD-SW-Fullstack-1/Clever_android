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
import com.example.clever.model.ToDoCompleteVO
import com.example.clever.model.ToDoVo
import com.example.clever.retrofit.RetrofitClient
import com.example.clever.utils.Time
import com.example.clever.view.home.todo.TodoDetailActivity
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ToDoTab1Adapter(
    val context: Context,
    val todoList: ArrayList<ToDoVo>,
    val onClickDeleteIcon: (todo: ToDoVo) -> Unit
) :
    RecyclerView.Adapter<ToDoTab1Adapter.ViewHolder>() {

    lateinit var loginSp: SharedPreferences
    private lateinit var memId: String
    val time = Time.getTime()

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val todolistImgType: ImageView
        val todolistTvTitle: TextView
        val todolistTvName: TextView
        val todolistImgCheck: ImageView
        val todoTab1Cl: ConstraintLayout

        init {
            todolistImgType = itemView.findViewById(R.id.todolistImgType)
            todolistTvTitle = itemView.findViewById(R.id.todolistTvTitle)
            todolistTvName = itemView.findViewById(R.id.todolistTvName)
            todolistImgCheck = itemView.findViewById(R.id.todolistImgCheck)
            todoTab1Cl = itemView.findViewById(R.id.todoTab1Cl)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(context)
        val view = layoutInflater.inflate(R.layout.template_todo_tab1_rv, null)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, @SuppressLint("RecyclerView") position: Int) {
        loginSp = context.getSharedPreferences("loginInfo", Context.MODE_PRIVATE)
        memId = loginSp.getString("mem_id", "").toString()

        holder.todolistTvTitle.text = todoList[position].todo_title
        holder.todolistTvName.text = todoList[position].mem_name

        if (todoList[position].todo_method == "사진") {
            holder.todolistImgType.setImageResource(R.drawable.camera)
            if (todoList[position].select_day.toString() == time) {
                holder.todolistImgCheck.setOnClickListener {

                    val req = ToDoCompleteVO(
                        todoList[position].todo_seq,
                        memId,
                        "",
                        "N",
                        todoList[position].cate_seq!!,
                    )

                    RetrofitClient.api.todoCmpl(req).enqueue(object : Callback<ResponseBody> {
                        override fun onResponse(
                            call: Call<ResponseBody>,
                            response: Response<ResponseBody>
                        ) {
                            val res = response.body()?.string()
                            Log.d("todoList adapter api", "네모")
                            onClickDeleteIcon.invoke(todoList[position])
                        }

                        override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                            TODO("Not yet implemented")
                        }
                    })
                }
            }
        } else {
            holder.todolistImgType.setImageResource(R.drawable.todo_check)
            if (todoList[position].select_day.toString() == time) {
                holder.todolistImgCheck.setOnClickListener {
                    val req = ToDoCompleteVO(
                        todoList[position].todo_seq,
                        memId,
                        "",
                        "",
                        "N",
                        todoList[position].cate_seq!!,
                    )

                    RetrofitClient.api.todoCmpl(req).enqueue(object : Callback<ResponseBody> {
                        override fun onResponse(
                            call: Call<ResponseBody>,
                            response: Response<ResponseBody>
                        ) {
                            val res = response.body()?.string()
                            onClickDeleteIcon.invoke(todoList[position])
                        }

                        override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                            TODO("Not yet implemented")
                        }
                    })
                }
            }
        }

        holder.todoTab1Cl.setOnClickListener {
            if (todoList[position].select_day.toString() == time) {
                val intent = Intent(context, TodoDetailActivity::class.java)
                intent.putExtra("todo_seq", todoList[position].todo_seq.toString())
                intent.putExtra("cate_seq", todoList[position].cate_seq.toString())
                context.startActivity(intent)
            }
        }
    }

    override fun getItemCount(): Int {
        return todoList.size
    }
}