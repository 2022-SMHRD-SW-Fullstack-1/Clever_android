package com.example.clever.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.clever.R
import com.example.clever.model.GroupVO

class MoreMemberAdapter(val context: Context, private val memberList: ArrayList<GroupVO>) :
    RecyclerView.Adapter<MoreMemberAdapter.ViewHolder>() {
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val moreMemRvPic: ImageView
        val moreMemRvName: TextView
        val moreMemRvDate: TextView
        val moreMemRvOut: ImageView

        init {
            moreMemRvPic = itemView.findViewById(R.id.moreMemRvPic)
            moreMemRvName = itemView.findViewById(R.id.moreMemRvName)
            moreMemRvDate = itemView.findViewById(R.id.moreMemRvDate)
            moreMemRvOut = itemView.findViewById(R.id.moreMemRvOut)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(context)
        val view = layoutInflater.inflate(R.layout.template_more_mem_rv, null)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val date = memberList[position].join_dt
        val year = date?.substring(0, 4)
        val month = date?.substring(5, 7)
        val day = date?.substring(8, 10)
        holder.moreMemRvDate.text = "${year}.${month}.${day} 합류"

        holder.moreMemRvName.text = memberList[position].mem_name
    }

    override fun getItemCount(): Int {
        return memberList.size
    }

}