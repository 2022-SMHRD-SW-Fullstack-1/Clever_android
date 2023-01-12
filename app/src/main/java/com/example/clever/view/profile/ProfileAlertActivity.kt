package com.example.clever.view.profile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.clever.R
import com.example.clever.databinding.ActivityProfileAlertBinding

class ProfileAlertActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileAlertBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_alert)

        binding = ActivityProfileAlertBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.profileAlertSwitch.setOnCheckedChangeListener { compoundButton, b ->
            if(b){
                binding.profileAlertTv.text = "ON"
            }else{
                binding.profileAlertTv.text = "OFF"
            }
        }
    }
}