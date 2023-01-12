package com.example.clever.view.profile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.clever.R
import com.example.clever.databinding.ActivityProfileBinding
import com.example.clever.view.InquiryActivity
import com.example.clever.view.LoginActivity

class ProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.profileImgChange.setOnClickListener {
            val intent = Intent(this@ProfileActivity, ProfileChangeActivity::class.java)
            startActivity(intent)
        }

        binding.profileLlInquiry.setOnClickListener {
            val intent = Intent(this@ProfileActivity, InquiryActivity::class.java)
            startActivity(intent)
        }

        binding.profilellSetting.setOnClickListener {
            val intent = Intent(this@ProfileActivity, ProfileSettingActivity::class.java)
            startActivity(intent)
        }

        binding.profileImgBack.setOnClickListener {
            finish()
        }

        binding.profilellLogout.setOnClickListener {
            val intent = Intent(this@ProfileActivity, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
    }
}