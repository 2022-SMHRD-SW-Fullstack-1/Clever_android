package com.example.clever.view.profile

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.clever.R
import com.example.clever.databinding.ActivityProfileBinding
import com.example.clever.model.GroupVO
import com.example.clever.model.Member
import com.example.clever.retrofit.RetrofitClient
import com.example.clever.view.notice.InquiryActivity
import com.example.clever.view.LoginActivity
import com.example.clever.view.notice.NoticeActivity
import com.example.clever.view.notice.TermsOfUseActivity
import com.google.gson.Gson
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
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

        binding.profileBtnJoinGroup.setOnClickListener {
            joinGroup()
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

    private fun joinGroup(){
        val view = layoutInflater.inflate(R.layout.custom_dialog_alert_input, null)
        val dialog = AlertDialog.Builder(this@ProfileActivity, R.style.CustomAlertDialog).setView(view).create()

        val alertInputTvTitle = view.findViewById<TextView>(R.id.alertInputTvTitle)
        val alertInputEt = view.findViewById<EditText>(R.id.alertInputEt)
        val alertInputBtnCancel = view.findViewById<Button>(R.id.alertInputBtnCancel)
        val alertInputBtnAccept = view.findViewById<TextView>(R.id.alertInputBtnAccept)

        alertInputTvTitle.text = "코드입력"

        alertInputBtnAccept.setOnClickListener {
            val group_serial = alertInputEt.text.toString()
            val req = GroupVO(null, null, null, group_serial, memId, null, null)
            RetrofitClient.api.joinGroup(req).enqueue(object : Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    val res = response.body()?.string()
                    if(res.toString() == "1"){
                        Toast.makeText(this@ProfileActivity, "그룹 추가 되었습니다.", Toast.LENGTH_SHORT).show()
                        dialog.dismiss()
                    }else{
                        Toast.makeText(this@ProfileActivity, "코드를 확인해주세요", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    TODO("Not yet implemented")
                }
            })
        }
        alertInputBtnCancel.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }
}