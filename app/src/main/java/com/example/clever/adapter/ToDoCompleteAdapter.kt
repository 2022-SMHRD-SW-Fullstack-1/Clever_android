package com.example.clever.adapter

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.opengl.Visibility
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.clever.R
import com.example.clever.model.ToDoCompleteVO

class ToDoCompleteAdapter(val context: Context, val todoCmplList: ArrayList<ToDoCompleteVO>) :
    RecyclerView.Adapter<ToDoCompleteAdapter.ViewHolder>() {

    lateinit var loginSp: SharedPreferences
    private lateinit var memId: String

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val todoCplRvName: TextView
        val todoCplRvTvDate: TextView
        val todoCplRvImgPic: ImageView
        val todoCplRvClBg: ConstraintLayout
        val todoCplRvClPic: ConstraintLayout
        val todoCplRvBtnWrite: Button
        val todoCplRvMemo: TextView
        val todoCplRvMemoCl: ConstraintLayout
        val todoCplRvMemoImg: ImageView
        val todoCplRvMemoDelete: ImageView

        init {
            loginSp = context.getSharedPreferences("loginInfo", Context.MODE_PRIVATE)
            memId = loginSp.getString("mem_id", "").toString()

            todoCplRvName = itemView.findViewById(R.id.todoCplRvName)
            todoCplRvTvDate = itemView.findViewById(R.id.todoCplRvTvDate)
            todoCplRvImgPic = itemView.findViewById(R.id.todoCplRvImgPic)
            todoCplRvClBg = itemView.findViewById(R.id.todoCplRvClBg)
            todoCplRvClPic = itemView.findViewById(R.id.todoCplRvClPic)
            todoCplRvBtnWrite = itemView.findViewById(R.id.todoCplRvBtnWrite)
            todoCplRvMemo = itemView.findViewById(R.id.todoCplRvMemo)
            todoCplRvMemoCl = itemView.findViewById(R.id.todoCplRvMemoCl)
            todoCplRvMemoImg = itemView.findViewById(R.id.todoCplRvMemoImg)
            todoCplRvMemoDelete = itemView.findViewById(R.id.todoCplRvMemoDelete)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(context)
        val view = layoutInflater.inflate(R.layout.template_todo_cpl_rv, null)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.todoCplRvName.text = todoCmplList[position].mem_name

        if (todoCmplList[position].cmpl_memo == null){
            holder.todoCplRvMemoCl.visibility = View.GONE
        }else{
            holder.todoCplRvBtnWrite.visibility = View.GONE
        }
        holder.todoCplRvMemo.text = todoCmplList[position].cmpl_memo

        if (todoCmplList[position].cmpl_img == null) {
            holder.todoCplRvClBg.setBackgroundColor(
                Color.parseColor(
                    "#000000"
                )
            )
            holder.todoCplRvClPic.visibility = View.GONE
        }

        val date = todoCmplList[position].cmpl_time!!
        val year = date.substring(0, 4)
        val month = date.substring(5, 7)
        val day = date.substring(8, 10)
        val hours = date.substring(11, 13)
        val min = date.substring(14, 16)
        holder.todoCplRvTvDate.text = "${year}.${month}.${day} ${hours}:${min}"

        if (todoCmplList[position].mem_id != memId) {
            holder.todoCplRvBtnWrite.visibility = View.GONE
            holder.todoCplRvMemoImg.visibility = View.GONE
            holder.todoCplRvMemoDelete.visibility = View.GONE
        }

    }

    override fun getItemCount(): Int {
        return todoCmplList.size
    }
}