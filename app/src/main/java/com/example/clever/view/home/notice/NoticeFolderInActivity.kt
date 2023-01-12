package com.example.clever.view.home.notice

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.clever.R
import com.example.clever.databinding.ActivityHomeBinding
import com.example.clever.databinding.ActivityNoticeFolderInBinding

class NoticeFolderInActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNoticeFolderInBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notice_folder_in)

        binding = ActivityNoticeFolderInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.noticeFolderMove.setOnClickListener {
            val intent = Intent(this@NoticeFolderInActivity, NoticeContentActivity::class.java)
            startActivity(intent)
        }

        binding.noticeFolderBack.setOnClickListener {
            finish()
        }
    }
}