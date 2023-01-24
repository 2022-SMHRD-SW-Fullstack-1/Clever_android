package com.example.clever.view.home.notice

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.clever.R
import com.example.clever.adapter.NoticeListAdapter
import com.example.clever.databinding.ActivityHomeBinding
import com.example.clever.databinding.ActivityNoticeFolderInBinding
import com.example.clever.model.NoticeVO
import com.example.clever.retrofit.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NoticeFolderInActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNoticeFolderInBinding

    lateinit var cate_seq: String
    lateinit var cate_name: String
    lateinit var adapter: NoticeListAdapter

    val noticeList = ArrayList<NoticeVO>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notice_folder_in)

        cate_seq = intent.getStringExtra("cate_seq").toString()
        cate_name = intent.getStringExtra("cate_name").toString()

        binding = ActivityNoticeFolderInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // container
        // noticeFolderRv

        // template
        // template_notice_folder_rv

        // item
        getNoticeList()

        // adapter
        adapter = NoticeListAdapter(
            this@NoticeFolderInActivity,
            noticeList
        )

        // adapter, container 연결
        binding.noticeFolderRv.adapter = adapter
        binding.noticeFolderRv.layoutManager = LinearLayoutManager(this@NoticeFolderInActivity)

        // event처리

        binding.noticeFolderTitle.text = cate_name

        binding.noticeFolderBack.setOnClickListener {
            finish()
        }

        binding.noticeFolderBtn.setOnClickListener {
            val intent = Intent(this@NoticeFolderInActivity, NoticeWriteActivity::class.java)
            intent.putExtra("cate_seq", cate_seq)
            intent.putExtra("cate_name", cate_name)
            startActivity(intent)
        }
    }

    private fun getNoticeList() {

        val req = NoticeVO(cate_seq.toInt())

        RetrofitClient.api.getNotice(req).enqueue(object : Callback<List<NoticeVO>> {
            override fun onResponse(
                call: Call<List<NoticeVO>>,
                response: Response<List<NoticeVO>>
            ) {
                val res = response.body()
                for (i in 0 until res!!.size) {
                    noticeList.add(res[i])
                }
                noticeList.reverse()
                adapter.notifyDataSetChanged()
            }

            override fun onFailure(call: Call<List<NoticeVO>>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })
    }
}