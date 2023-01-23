package com.example.clever.adapter

import android.app.AlertDialog
import android.content.Context
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.example.clever.R
import com.example.clever.model.NoticeCommentVO
import com.example.clever.retrofit.RetrofitClient
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NoticeCommentAdapter(val context: Context, val commentList: ArrayList<NoticeCommentVO>) :
    RecyclerView.Adapter<NoticeCommentAdapter.ViewHolder>() {

    lateinit var loginSp: SharedPreferences
    private lateinit var memId: String

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val noticeComTvName: TextView
        val noticeComTvTime: TextView
        val noticeComTvContent: TextView
        val noticeComImgMore: ImageView

        init {
            noticeComTvName = itemView.findViewById(R.id.noticeComTvName)
            noticeComTvTime = itemView.findViewById(R.id.noticeComTvTime)
            noticeComTvContent = itemView.findViewById(R.id.noticeComTvContent)
            noticeComImgMore = itemView.findViewById(R.id.noticeComImgMore)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(context)
        val view = layoutInflater.inflate(R.layout.template_notice_comment_rv, null)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.noticeComTvName.text = commentList[position].mem_name
        holder.noticeComTvContent.text = commentList[position].com_content

        var time = commentList[position].com_time.toString().substring(0, 16)
        holder.noticeComTvTime.text = time

        loginSp = context.getSharedPreferences("loginInfo", Context.MODE_PRIVATE)
        memId = loginSp.getString("mem_id", "").toString()
        if (commentList[position].mem_id.toString() != memId) {
            holder.noticeComImgMore.visibility = View.GONE
        } else {
            holder.noticeComImgMore.setOnClickListener {
                val layoutInflater = LayoutInflater.from(context)
                val view = layoutInflater.inflate(R.layout.custom_dialog_alert, null)

                val alertDialog = AlertDialog.Builder(context, R.style.CustomAlertDialog)
                    .setView(view).create()

                val alertBtnCancel = view.findViewById<ImageButton>(R.id.alertBtnCancel)
                val alertTvTitle = view.findViewById<TextView>(R.id.alertTvTitle)
                val alertTvContent = view.findViewById<TextView>(R.id.alertTvContent)
                alertTvContent.visibility = View.GONE
                val alertBtnOk = view.findViewById<Button>(R.id.alertBtnOk)

                alertTvTitle.text = "댓글삭제"

                alertBtnOk.setOnClickListener {
                    val req = NoticeCommentVO(commentList[position].com_seq, null, null, null, null, null)
                    RetrofitClient.api.deleteComment(req).enqueue(object : Callback<ResponseBody> {
                        override fun onResponse(
                            call: Call<ResponseBody>,
                            response: Response<ResponseBody>
                        ) {
                            val res = response.body()?.string()
                            if (res.toString() == "1") {
                                Toast.makeText(
                                    context,
                                    "댓글을 삭제했습니다.",
                                    Toast.LENGTH_SHORT
                                ).show()
                                alertDialog.dismiss()
                            }else{
                                Toast.makeText(context, "댓글 삭제 실패", Toast.LENGTH_SHORT).show()
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
    }

    override fun getItemCount(): Int {
        return commentList.size
    }
}