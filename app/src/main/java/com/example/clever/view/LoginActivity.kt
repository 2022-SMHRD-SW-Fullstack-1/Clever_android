package com.example.clever.view

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
import com.example.clever.databinding.ActivityLoginBinding
import com.example.clever.model.Member
import com.example.clever.retrofit.RetrofitClient
import com.google.gson.Gson
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    lateinit var autoSp: SharedPreferences

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        autoSp = getSharedPreferences("autoLoginInfo", Context.MODE_PRIVATE)

        binding.loginCl.setOnTouchListener { _, _ ->
            hideKeyboard()
            false
        }

        binding.loginBtnLogin.setOnClickListener {
            login()
        }

        binding.loginTvJoin.setOnClickListener {
            val intent = Intent(this@LoginActivity, JoinActivity::class.java)
            startActivity(intent)
        }

        binding.loginClGoPw.setOnClickListener {
            val intent = Intent(this@LoginActivity, PasswordActivity::class.java)
            startActivity(intent)
        }
    }

    fun hideKeyboard() {
        if (this.currentFocus != null) {
            val inputManager: InputMethodManager =
                this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputManager.hideSoftInputFromWindow(
                this.currentFocus!!.windowToken,
                InputMethodManager.HIDE_NOT_ALWAYS
            )
        }
    }

    private fun login() {
        val phone = binding.loginEtPhone.text.toString().trim()
        val pw = binding.loginEtPw.text.toString().trim()

        if (phone == "") {
            Toast.makeText(this@LoginActivity, "휴대폰번호를 입력해주세요", Toast.LENGTH_SHORT).show()
        } else {
            if (pw == "") {
                Toast.makeText(this@LoginActivity, "비밀번호를 입력해주세요", Toast.LENGTH_SHORT).show()
            } else {
                val loginInfo = Member(phone, pw)

                RetrofitClient.api.login(loginInfo).enqueue(object : Callback<ResponseBody> {
                    override fun onResponse(
                        call: Call<ResponseBody>,
                        response: Response<ResponseBody>
                    ) {
                        val res = response.body()?.string()
                        if (res != "") {
                            val memberInfo = Gson().fromJson(res, Member::class.java)
                            Log.d("test login id", memberInfo.mem_id)
                            Log.d("test login name", memberInfo.mem_name.toString())
                        }
//                val intent = Intent(this@LoginActivity, MainActivity::class.java)
//                startActivity(intent)
                    }

                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                        t.localizedMessage?.let { Log.d("hello", it) }
                    }
                })
            }
        }
    }
}