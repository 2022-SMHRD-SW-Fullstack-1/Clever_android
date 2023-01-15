package com.example.clever.view.profile

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.clever.R
import com.example.clever.databinding.ActivityProfileChangeBinding

class ProfileChangeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileChangeBinding

    lateinit var loginSp: SharedPreferences
    private lateinit var memId: String
    private lateinit var memName:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_change)

        loginSp = getSharedPreferences("loginInfo", Context.MODE_PRIVATE)
        memId = loginSp.getString("mem_id", "").toString()
        memName = loginSp.getString("mem_name","").toString()

        binding = ActivityProfileChangeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.profileChangeEtLastName.setText(memName)
        binding.profileChangeEtPhone.setText(memId)
        binding.profileChangeEtPhone.isEnabled = false

        binding.profileChangeImgBack.setOnClickListener {
            finish()
        }

        binding.profileChangeBtnProfile.setOnClickListener {
            finish()
        }
    }
}