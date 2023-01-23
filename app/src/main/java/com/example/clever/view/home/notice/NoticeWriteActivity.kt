package com.example.clever.view.home.notice

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.clever.R
import com.example.clever.databinding.ActivityNoticeWriteBinding
import com.example.clever.model.NoticeVO
import com.example.clever.retrofit.RetrofitClient
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NoticeWriteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNoticeWriteBinding

    lateinit var loginSp: SharedPreferences
    private lateinit var memId:String

    lateinit var cate_seq: String
    lateinit var cate_name: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notice_write)

        loginSp = getSharedPreferences("loginInfo", Context.MODE_PRIVATE)
        memId = loginSp.getString("mem_id", "").toString()

        cate_seq = intent.getStringExtra("cate_seq").toString()
        cate_name = intent.getStringExtra("cate_name").toString()

        binding = ActivityNoticeWriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.noticeWriteBack.setOnClickListener {
            finish()
        }

        binding.noticeWriteTitle.text = cate_name

        binding.NoticeWriteBtn.setOnClickListener {
            noticeWrite()
        }
    }

    private fun noticeWrite(){
        val title = binding.NoticeWriteEtTitle.text.toString()
        val content = binding.NoticeWriteEtContent.text.toString()
        val req = NoticeVO(cate_seq.toInt(), title, content, null, memId)
        RetrofitClient.api.noticeWrite(req).enqueue(object : Callback<ResponseBody>{
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                val res = response.body()?.string()
                if(res.toString() == "1"){
                    Toast.makeText(this@NoticeWriteActivity, "글 작성 성공", Toast.LENGTH_SHORT).show()
                    finish()
                }else{
                    Toast.makeText(this@NoticeWriteActivity, "글 작성에 실패했습니다.", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })
    }
}