package com.example.clever.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.clever.R
import com.example.clever.model.ToDoCompleteVO
import com.example.clever.view.home.todo.TodoCompleteActivity

class ToDoTab2Adapter(val context: Context, val cmplList:ArrayList<ToDoCompleteVO>):RecyclerView.Adapter<ToDoTab2Adapter.ViewHolder>() {
    inner class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {

        val todoTab2TvTitle:TextView
        val todoTab2TvName:TextView
        val todoTab2Cl: ConstraintLayout

        init {
            todoTab2TvTitle = itemView.findViewById(R.id.todoTab2TvTitle)
            todoTab2TvName = itemView.findViewById(R.id.todoTab2TvName)
            todoTab2Cl = itemView.findViewById(R.id.todoTab2Cl)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(context)
        val view = layoutInflater.inflate(R.layout.template_todo_tab2_rv, null)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.todoTab2TvTitle.text = cmplList[position].todo_title
        holder.todoTab2TvName.text = cmplList[position].mem_name

        holder.todoTab2Cl.setOnClickListener {
            val intent = Intent(context, TodoCompleteActivity::class.java)
            intent.putExtra("todo_seq", cmplList[position].todo_seq.toString())
            intent.putExtra("todo_title", cmplList[position].todo_title.toString())
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return cmplList.size
    }
}