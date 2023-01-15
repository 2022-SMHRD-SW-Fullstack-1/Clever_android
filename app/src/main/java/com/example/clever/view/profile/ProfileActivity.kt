package com.example.clever.view.profile

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.clever.R
import com.example.clever.databinding.ActivityProfileBinding
import com.example.clever.model.Member
import com.example.clever.view.notice.InquiryActivity
import com.example.clever.view.LoginActivity
import com.example.clever.view.notice.NoticeActivity
import com.example.clever.view.notice.TermsOfUseActivity
import com.google.gson.Gson
import kotlin.math.log
import kotlin.properties.Delegates

class ProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding

    lateinit var loginSp: SharedPreferences
    private lateinit var memId: String
    private lateinit var memName:String

    lateinit var autoSp: SharedPreferences
    private lateinit var autoLoginPhone: String
    private lateinit var autoLoginPw: String
    private var autoLoginCheckBox by Delegates.notNull<Boolean>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        // 로그인한 유저 정보 저장
        loginSp = getSharedPreferences("loginInfo", Context.MODE_PRIVATE)
        memId = loginSp.getString("mem_id", "").toString()
        memName = loginSp.getString("mem_name","").toString()

        // 자동로그인 정보 저장
        autoSp = getSharedPreferences("autoLoginInfo", Context.MODE_PRIVATE)
        autoLoginPhone = autoSp.getString("loginPhone", "").toString()
        autoLoginPw = autoSp.getString("loginPw", "").toString()
        autoLoginCheckBox = autoSp.getBoolean("loginCb", false)

        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.profileTvName.text = memName
        binding.profileTvPhone.text =memId

        binding.profileClChange.setOnClickListener {
            val intent = Intent(this@ProfileActivity, ProfileChangeActivity::class.java)
            startActivity(intent)
        }

        binding.profilellNotice.setOnClickListener {
            val intent = Intent(this@ProfileActivity, NoticeActivity::class.java)
            startActivity(intent)
        }

        binding.profilellUse.setOnClickListener {
            val intent = Intent(this@ProfileActivity, TermsOfUseActivity::class.java)
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
            logOut()
        }
    }

    private fun logOut() {
        val editorAuto = autoSp.edit()

        editorAuto.putString("loginPhone", "")
        editorAuto.putString("loginPw", "")
        editorAuto.putBoolean("loginCb", false)
        editorAuto.apply()

        val editorMem = loginSp.edit()
        editorMem.putString("mem_id", "")
        editorMem.putString("mem_name", "")
        editorMem.apply()

        val intent = Intent(this@ProfileActivity, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }
}