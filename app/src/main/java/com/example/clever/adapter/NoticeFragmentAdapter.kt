package com.example.clever.adapter

import android.content.Context
import android.content.Intent
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
import com.example.clever.model.NoticeVO
import com.example.clever.retrofit.RetrofitClient
import com.example.clever.view.home.notice.NoticeFolderInActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NoticeFragmentAdapter(val context: Context, val categoryList: ArrayList<CategoryVO>) :
    RecyclerView.Adapter<NoticeFragmentAdapter.ViewHolder>() {
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val noticeCategoryCl: ConstraintLayout
        val noticeCategoryTvTitle: TextView
        val noticeCategoryTvCount: TextView
        val noticeCategoryImgMore: ImageView

        init {

            noticeCategoryCl = itemView.findViewById(R.id.noticeCategoryCl)
            noticeCategoryTvTitle = itemView.findViewById(R.id.noticeCategoryTvTitle)
            noticeCategoryTvCount = itemView.findViewById(R.id.noticeCategoryTvCount)
            noticeCategoryImgMore = itemView.findViewById(R.id.noticeCategoryImgMore)

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(context)
        val view = layoutInflater.inflate(R.layout.template_notice_category_rv, null)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.noticeCategoryTvTitle.text = categoryList[position].cate_name

        var res: Int
        val req = NoticeVO(categoryList[position].cate_seq!!.toInt())
        RetrofitClient.api.getNotice(req).enqueue(object : Callback<List<NoticeVO>> {
            override fun onResponse(
                call: Call<List<NoticeVO>>,
                response: Response<List<NoticeVO>>
            ) {
                res = response.body()!!.size
                holder.noticeCategoryTvCount.text = "$res 개"
            }

            override fun onFailure(call: Call<List<NoticeVO>>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })

        holder.noticeCategoryCl.setOnClickListener {
            val intent = Intent(context, NoticeFolderInActivity::class.java)
            intent.putExtra("cate_seq", "${categoryList[position].cate_seq}")
            intent.putExtra("cate_name", categoryList[position].cate_name)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return categoryList.size
    }
}