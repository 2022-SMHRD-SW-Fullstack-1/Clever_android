package com.example.clever.view.profile

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.example.clever.R
import com.example.clever.databinding.ActivityProfilePwBinding
import com.example.clever.model.Member
import com.example.clever.retrofit.RetrofitClient
import com.example.clever.view.LoginActivity
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfilePwActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfilePwBinding

    lateinit var loginSp: SharedPreferences
    private lateinit var memId: String

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_pw)

        loginSp = getSharedPreferences("loginInfo", Context.MODE_PRIVATE)
        memId = loginSp.getString("mem_id", "").toString()

        binding = ActivityProfilePwBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.profilePwCl.setOnTouchListener { _, _ ->
            hideKeyboard()
            false
        }

        binding.profilePwImgBack.setOnClickListener {
            finish()
        }

        binding.profilePwBtnPwCh.setOnClickListener {
            chPw()
        }
    }

    private fun hideKeyboard() {
        val inputManager: InputMethodManager =
            this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(
            this.currentFocus!!.windowToken,
            InputMethodManager.HIDE_NOT_ALWAYS
        )
    }

    private fun chPw() {
        val newPw = binding.profilePwEtPw.text.toString()
        val newPwRe = binding.profilePwEtPwRe.text.toString()
        if (newPwRe == newPw) {
            val pw = binding.profilePwEtPwNow.text.toString()
            val req = Member(memId, pw)
            RetrofitClient.api.login(req).enqueue(object : Callback<ResponseBody>{
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    val res = response.body()?.string()
                    if (res != "") {
                        val chReq = Member(memId, newPw)
                        RetrofitClient.api.changePw(chReq).enqueue(object : Callback<ResponseBody>{
                            override fun onResponse(
                                call: Call<ResponseBody>,
                                response: Response<ResponseBody>
                            ) {
                                val res = response.body()?.string()
                                if (res.toString() == "1") {
                                    Toast.makeText(
                                        this@ProfilePwActivity,
                                        "비밀번호가 변경되었습니다.",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    finish()
                                } else {
                                    Toast.makeText(
                                        this@ProfilePwActivity,
                                        "비밀번호 변경을 실패하였습니다.",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }

                            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                                TODO("Not yet implemented")
                            }
                        })
                    }else{
                        Toast.makeText(this@ProfilePwActivity, "현재 비밀번호를 확인해주세요", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    TODO("Not yet implemented")
                }
            })

        } else {
            Toast.makeText(this@ProfilePwActivity, "새비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show()
        }
    }
}