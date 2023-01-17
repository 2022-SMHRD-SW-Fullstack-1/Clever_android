package com.example.clever.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.clever.R
import com.example.clever.model.ToDoVo
import com.example.clever.view.home.todo.TodoDetailActivity

class ToDoTab1Adapter(val context: Context, val todoList: ArrayList<ToDoVo>) :
    RecyclerView.Adapter<ToDoTab1Adapter.ViewHolder>() {
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

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.todolistTvTitle.text = todoList[position].todo_title
        holder.todolistTvName.text = todoList[position].mem_name

        holder.todoTab1Cl.setOnClickListener {
            val intent = Intent(context, TodoDetailActivity::class.java)
            intent.putExtra("todo_seq", todoList[position].todo_seq.toString())
            intent.putExtra("cate_seq", todoList[position].cate_seq.toString())
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return todoList.size
    }
}