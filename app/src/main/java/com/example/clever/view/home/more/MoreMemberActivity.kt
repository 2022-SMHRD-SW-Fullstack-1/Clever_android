package com.example.clever.view.home.more

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.clever.R
import com.example.clever.databinding.ActivityMoreMemberBinding

class MoreMemberActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMoreMemberBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_more_member)

        binding = ActivityMoreMemberBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.moreMemImgBack.setOnClickListener {
            finish()
        }
    }
}