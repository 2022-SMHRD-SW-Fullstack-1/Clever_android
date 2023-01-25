package com.example.clever.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.clever.R
import com.example.clever.model.ToDoCompleteVO
import com.example.clever.view.home.todo.TodoCompleteActivity

class TodoUniqueAdapter(val context: Context, val uniqueList: ArrayList<ToDoCompleteVO>) :
    RecyclerView.Adapter<TodoUniqueAdapter.ViewHolder>() {
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val todoUniqueRvTv: TextView

        init {
            todoUniqueRvTv = itemView.findViewById(R.id.todoUniqueRvTv)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(context)
        val view = layoutInflater.inflate(R.layout.template_todo_unique_rv, null)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.todoUniqueRvTv.text = "• ${uniqueList[position].cmpl_memo}"

        holder.todoUniqueRvTv.setOnClickListener {
            val intent = Intent(context, TodoCompleteActivity::class.java)
            intent.putExtra("todo_seq", uniqueList[position].todo_seq.toString())
            intent.putExtra("todo_title", uniqueList[position].todo_title.toString())
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return uniqueList.size
    }
}