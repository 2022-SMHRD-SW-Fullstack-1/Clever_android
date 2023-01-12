package com.example.clever.view.profile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.clever.R
import com.example.clever.databinding.ActivityProfileSettingBinding

class ProfileSettingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileSettingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_setting)

        binding = ActivityProfileSettingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.profileSettingImgBack.setOnClickListener {
            finish()
        }

        binding.profileSettingllAlert.setOnClickListener {
            val intent = Intent(this@ProfileSettingActivity, ProfileAlertActivity::class.java)
            startActivity(intent)
        }

        binding.profileSettingllPw.setOnClickListener {
            val intent = Intent(this@ProfileSettingActivity, ProfilePwActivity::class.java)
            startActivity(intent)
        }


    }
}