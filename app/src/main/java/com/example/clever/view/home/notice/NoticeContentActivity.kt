package com.example.clever.view.home.notice

import android.app.AlertDialog
import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.recyclerview.widget.GridLayoutManager
import com.example.clever.R
import com.example.clever.adapter.NoticeCommentAdapter
import com.example.clever.databinding.ActivityNoticeContentBinding
import com.example.clever.databinding.ActivityNoticeFolderInBinding
import com.example.clever.decorator.other.MainRvDecorator
import com.example.clever.model.CategoryVO
import com.example.clever.model.NoticeCommentVO
import com.example.clever.model.NoticeVO
import com.example.clever.retrofit.RetrofitClient
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NoticeContentActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNoticeContentBinding

    lateinit var notice_seq: String
    lateinit var cate_seq: String
    lateinit var notice_title: String

    lateinit var loginSp: SharedPreferences
    private lateinit var memId: String

    val commentList = ArrayList<NoticeCommentVO>()

    lateinit var adapter: NoticeCommentAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notice_content)

        notice_seq = intent.getStringExtra("notice_seq").toString()
        cate_seq = intent.getStringExtra("cate_seq").toString()

        loginSp = getSharedPreferences("loginInfo", Context.MODE_PRIVATE)
        memId = loginSp.getString("mem_id", "").toString()

        binding = ActivityNoticeContentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // container
        // noticeContentCommentRv

        // template
        // template_notice_comment_rv

        // item
        getComment()

        // adapter
        adapter = NoticeCommentAdapter(
            this@NoticeContentActivity,
            commentList
        )

        // 연결
        binding.noticeContentCommentRv.adapter = adapter
        binding.noticeContentCommentRv.layoutManager =
            GridLayoutManager(this@NoticeContentActivity, 1)
        binding.noticeContentCommentRv.addItemDecoration(MainRvDecorator(16))

        // event
        binding.noticeContentCl.setOnTouchListener { _, _ ->
            hideKeyboard()
            false
        }


        binding.noticeContentImgBack.setOnClickListener {
            finish()
        }

        binding.noticeContentImgMore.setOnClickListener {
            noticeDelete()
        }

        binding.btnWriteComment.setOnClickListener {
            writeComment()
        }

        getNoticeDetail()
    }

    private fun getNoticeDetail() {
        val req = NoticeVO(notice_seq.toInt(), cate_seq.toInt())

        RetrofitClient.api.getNoticeDetail(req).enqueue(object : Callback<NoticeVO> {
            override fun onResponse(call: Call<NoticeVO>, response: Response<NoticeVO>) {
                val res = response.body()!!
                notice_title = res.notice_title!!
                binding.noticeContentTvTitle.text = notice_title
                binding.noticeContentTvContent.text = res.notice_content
                binding.noticeContentTvName.text = res.mem_name
                // 2023년 1월 1일 12:45 작성
                val date = res.notice_dt!!
                var year = date.substring(0, 4)
                var month = date.substring(5, 7)
                if (month.toInt() < 10) month = month.substring(1)
                var day = date.substring(8, 10)
                if (day.toInt() < 10) day = day.substring(1)
                var hour = date.substring(11, 13)
                var min = date.substring(14, 16)
                binding.noticeContentTvDate.text = "${year}년 ${month}월 ${day}일 ${hour}:${min} 작성"

                if (res.mem_id != memId) binding.noticeContentImgMore.isVisible = false

//                binding.noticeContentImgPhoto.setImageResource()
            }

            override fun onFailure(call: Call<NoticeVO>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })
    }

    private fun noticeDelete() {
        val layoutInflater = LayoutInflater.from(this@NoticeContentActivity)
        val view = layoutInflater.inflate(R.layout.custom_dialog_alert, null)

        val alertDialog =
            AlertDialog.Builder(this@NoticeContentActivity, R.style.CustomAlertDialog)
                .setView(view).create()

        val alertBtnCancel = view.findViewById<ImageButton>(R.id.alertBtnCancel)
        val alertTvTitle = view.findViewById<TextView>(R.id.alertTvTitle)
        val alertTvContent = view.findViewById<TextView>(R.id.alertTvContent)
        val alertBtnOk = view.findViewById<Button>(R.id.alertBtnOk)

        alertTvTitle.text = "게시글 삭제"
        alertTvContent.text = "$notice_title 을/를 삭제 하시겠습니까 ? "

        alertBtnOk.setOnClickListener {
            val req = NoticeVO(notice_seq.toInt(), cate_seq.toInt())
            RetrofitClient.api.noticeDelete(req).enqueue(object : Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    val res = response.body()?.string()
                    if (res.toString() == "1") {
                        Toast.makeText(
                            this@NoticeContentActivity,
                            "$notice_title 을/를 삭제 하였습니다.",
                            Toast.LENGTH_SHORT
                        ).show()
                        finish()
                    } else {
                        Toast.makeText(
                            this@NoticeContentActivity,
                            "$notice_title 을/를 삭제에 실패했습니다..",
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

    private fun writeComment() {
        val comment = binding.noticeContentEtComment.text.toString()
        val req = NoticeCommentVO(null, notice_seq.toInt(), memId, null, comment, null)
        if (comment != "") {
            RetrofitClient.api.writeComment(req).enqueue(object : Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    val res = response.body()?.string()
                    if (res.toString() == "1") {
                        binding.noticeContentEtComment.text = null
                        getComment()
                        hideKeyboard()
                    } else {
                        Toast.makeText(
                            this@NoticeContentActivity,
                            "댓글 작성에 실패했습니다.",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    TODO("Not yet implemented")
                }
            })
        }
    }

    private fun getComment() {
        val req = NoticeCommentVO(notice_seq.toInt())
        RetrofitClient.api.getComment(req).enqueue(object : Callback<List<NoticeCommentVO>> {
            override fun onResponse(
                call: Call<List<NoticeCommentVO>>,
                response: Response<List<NoticeCommentVO>>
            ) {
                commentList.clear()
                val res = response.body()
                Log.d("getComment", res.toString())
                for (i in 0 until res!!.size) {
                    commentList.add(res[i])
                }
                adapter.notifyDataSetChanged()
            }

            override fun onFailure(call: Call<List<NoticeCommentVO>>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })
    }

    private fun hideKeyboard() {
        if (this.currentFocus != null) {
            val inputManager: InputMethodManager =
                this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputManager.hideSoftInputFromWindow(
                this.currentFocus!!.windowToken,
                InputMethodManager.HIDE_NOT_ALWAYS
            )
        }
    }
}