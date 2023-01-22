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
import com.example.clever.model.NoticeVO
import com.example.clever.view.home.notice.NoticeContentActivity

class NoticeListAdapter(val context: Context, val noticeList: ArrayList<NoticeVO>) :
    RecyclerView.Adapter<NoticeListAdapter.ViewHolder>() {
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val noticeFolderRvTitle: TextView
        val noticeFolderRvContent: TextView
        val noticeFolderRvName: TextView
        val noticeFolderRvDate: TextView
        val noticeClGoDetail: ConstraintLayout

        init {

            noticeFolderRvTitle = itemView.findViewById(R.id.noticeFolderRvTitle)
            noticeFolderRvContent = itemView.findViewById(R.id.noticeFolderRvContent)
            noticeFolderRvName = itemView.findViewById(R.id.noticeFolderRvName)
            noticeFolderRvDate = itemView.findViewById(R.id.noticeFolderRvDate)
            noticeClGoDetail = itemView.findViewById(R.id.noticeClGoDetail)

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(context)
        val view = layoutInflater.inflate(R.layout.template_notice_folder_rv, null)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.noticeFolderRvTitle.text = noticeList[position].notice_title
        holder.noticeFolderRvContent.text = noticeList[position].notice_content
        holder.noticeFolderRvName.text = noticeList[position].mem_name

        val date = noticeList[position].notice_dt!!
        var year = date.substring(0, 4)
        var month = date.substring(5, 7)
        if (month.toInt() < 10) month = month.substring(1)
        var day = date.substring(8, 10)
        if (day.toInt() < 10) day = day.substring(1)

        holder.noticeFolderRvDate.text = "${year}년 ${month}월 ${day}일 작성"

        holder.noticeClGoDetail.setOnClickListener {
            val intent = Intent(context, NoticeContentActivity::class.java)
            intent.putExtra("notice_seq", "${noticeList[position].notice_seq}")
            intent.putExtra("cate_seq", "${noticeList[position].cate_seq}")
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return noticeList.size
    }
}