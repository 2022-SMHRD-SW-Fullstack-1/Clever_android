package com.example.clever.view.home.cal

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.clever.R
import com.example.clever.databinding.ActivityCalculateBinding

class CalculateActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCalculateBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calculate)

        binding = ActivityCalculateBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.calCalculateImgBack.setOnClickListener {
            finish()
        }


    }
}