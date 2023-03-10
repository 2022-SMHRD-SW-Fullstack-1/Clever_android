package com.example.clever.adapter

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.clever.R
import com.example.clever.model.CategoryVO
import com.example.clever.model.GroupVO
import com.example.clever.model.NoticeVO
import com.example.clever.retrofit.RetrofitClient
import com.example.clever.view.home.notice.NoticeFolderInActivity
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NoticeFragmentAdapter(val context: Context, val categoryList: ArrayList<CategoryVO>) :
    RecyclerView.Adapter<NoticeFragmentAdapter.ViewHolder>() {

    lateinit var loginSp: SharedPreferences
    private lateinit var memId: String

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val noticeCategoryCl: ConstraintLayout
        val noticeCategoryTvTitle: TextView
        val noticeCategoryTvCount: TextView
        val noticeCategoryImgMore: ImageView

        init {
            loginSp = context.getSharedPreferences("loginInfo", Context.MODE_PRIVATE)
            memId = loginSp.getString("mem_id", "").toString()

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
        val reqCount = NoticeVO(categoryList[position].cate_seq!!.toInt())
        RetrofitClient.api.getNotice(reqCount).enqueue(object : Callback<List<NoticeVO>> {
            override fun onResponse(
                call: Call<List<NoticeVO>>,
                response: Response<List<NoticeVO>>
            ) {
                res = response.body()!!.size
                holder.noticeCategoryTvCount.text = "$res ???"
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

        val reqLeader = GroupVO(categoryList[position].group_seq!!)
        RetrofitClient.api.groupInfo(reqLeader).enqueue(object : Callback<GroupVO> {
            override fun onResponse(call: Call<GroupVO>, response: Response<GroupVO>) {
                val res = response.body()!!
                if (res.mem_id == memId) holder.noticeCategoryImgMore.isVisible = true
            }

            override fun onFailure(call: Call<GroupVO>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })

        holder.noticeCategoryImgMore.setOnClickListener {
            val layoutInflater = LayoutInflater.from(context)
            val view = layoutInflater.inflate(R.layout.custom_dialog_alert, null)

            val alertDialog = AlertDialog.Builder(context, R.style.CustomAlertDialog)
                .setView(view).create()

            val alertBtnCancel = view.findViewById<ImageButton>(R.id.alertBtnCancel)
            val alertTvTitle = view.findViewById<TextView>(R.id.alertTvTitle)
            val alertTvContent = view.findViewById<TextView>(R.id.alertTvContent)
            val alertBtnOk = view.findViewById<Button>(R.id.alertBtnOk)

            alertTvTitle.text = "?????? ??????"
            alertTvContent.text = "${categoryList[position].cate_name} ????????? ???????????????????????? ?"

            alertBtnOk.setOnClickListener {
                val cate_seq = categoryList[position].cate_seq

                val req = CategoryVO(cate_seq!!)
                RetrofitClient.api.categoryDelete(req).enqueue(object : Callback<ResponseBody> {
                    override fun onResponse(
                        call: Call<ResponseBody>,
                        response: Response<ResponseBody>
                    ) {
                        val res = response.body()?.string()
                        if (res.toString() == "1") {
                            Toast.makeText(
                                context,
                                "${categoryList[position].cate_name} ????????? ??????????????????.",
                                Toast.LENGTH_SHORT
                            ).show()
                            alertDialog.dismiss()
                        } else {
                            Toast.makeText(
                                context,
                                "${categoryList[position].cate_name} ????????? ????????? ??????????????????.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                        TODO("Not yet implemented")
                    }
                })

            }
            alertBtnCancel.setOnClickListener {
                alertDialog.dismiss()
            }
            alertDialog.show()
        }
    }

    override fun getItemCount(): Int {
        return categoryList.size
    }
}