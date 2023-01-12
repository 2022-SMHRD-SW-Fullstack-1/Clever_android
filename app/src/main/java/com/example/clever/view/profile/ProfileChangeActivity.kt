package com.example.clever.view.profile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.clever.R
import com.example.clever.databinding.ActivityProfileChangeBinding

class ProfileChangeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileChangeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_change)

        binding = ActivityProfileChangeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.profileChangeImgBack.setOnClickListener {
            finish()
        }

        binding.profileChangeBtnProfile.setOnClickListener {
            finish()
        }
    }
}