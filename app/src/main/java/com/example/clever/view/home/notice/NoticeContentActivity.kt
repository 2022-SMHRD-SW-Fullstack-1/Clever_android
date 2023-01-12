package com.example.clever.view.home.notice

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.clever.R
import com.example.clever.databinding.ActivityNoticeContentBinding
import com.example.clever.databinding.ActivityNoticeFolderInBinding

class NoticeContentActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNoticeContentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notice_content)

        binding = ActivityNoticeContentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.noticeContentImgBack.setOnClickListener {
            finish()
        }
    }
}