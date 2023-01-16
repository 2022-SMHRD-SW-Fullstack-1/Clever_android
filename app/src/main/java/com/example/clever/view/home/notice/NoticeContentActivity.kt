package com.example.clever.view.home.notice

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.isVisible
import com.example.clever.R
import com.example.clever.databinding.ActivityNoticeContentBinding
import com.example.clever.databinding.ActivityNoticeFolderInBinding
import com.example.clever.model.NoticeVO
import com.example.clever.retrofit.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NoticeContentActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNoticeContentBinding

    lateinit var notice_seq: String
    lateinit var cate_seq: String

    lateinit var loginSp: SharedPreferences
    private lateinit var memId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notice_content)

        notice_seq = intent.getStringExtra("notice_seq").toString()
        cate_seq = intent.getStringExtra("cate_seq").toString()

        loginSp = getSharedPreferences("loginInfo", Context.MODE_PRIVATE)
        memId = loginSp.getString("mem_id", "").toString()

        binding = ActivityNoticeContentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.noticeContentImgBack.setOnClickListener {
            finish()
        }

        binding.noticeContentImgMore.setOnClickListener {

        }

        getNoticeDetail()
    }

    private fun getNoticeDetail() {
        val req = NoticeVO(notice_seq.toInt(), cate_seq.toInt())

        RetrofitClient.api.getNoticeDetail(req).enqueue(object : Callback<NoticeVO> {
            override fun onResponse(call: Call<NoticeVO>, response: Response<NoticeVO>) {
                val res = response.body()!!
                binding.noticeContentTvTitle.text = res.notice_title
                binding.noticeContentTvContent.text = res.notice_content
                binding.noticeContentTvName.text = res.mem_name
                // 2023년 1월 1일 오후 12:45 작성
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

            }

            override fun onFailure(call: Call<NoticeVO>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })

    }
}