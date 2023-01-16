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
import com.example.clever.model.CategoryVO
import com.example.clever.view.home.todo.TodoListActivity

class TodoFragmentAdapter(val context: Context, val categoryList: ArrayList<CategoryVO>) :
    RecyclerView.Adapter<TodoFragmentAdapter.ViewHolder>() {
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val todoCategoryTvTitle: TextView
        val todoCategoryTvState: TextView
        val todoCategoryImgPin: ImageView
        val todoCategoryCl: ConstraintLayout

        init {
            todoCategoryTvTitle = itemView.findViewById(R.id.todoCategoryTvTitle)
            todoCategoryTvState = itemView.findViewById(R.id.todoCategoryTvState)
            todoCategoryImgPin = itemView.findViewById(R.id.todoCategoryImgPin)
            todoCategoryCl = itemView.findViewById(R.id.todoCategoryCl)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(context)
        val view = layoutInflater.inflate(R.layout.template_todo_category_rv, null)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.todoCategoryTvTitle.text = categoryList[position].cate_name

        holder.todoCategoryCl.setOnClickListener {
            val intent = Intent(context, TodoListActivity::class.java)
            intent.putExtra("cate_seq", "${categoryList[position].cate_seq}")
            intent.putExtra("cate_name", categoryList[position].cate_name)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return categoryList.size
    }
}