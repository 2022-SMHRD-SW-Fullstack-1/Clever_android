package com.example.clever.view

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
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
import kotlin.properties.Delegates

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    lateinit var loginSp: SharedPreferences
    private lateinit var memId:String
    private lateinit var memName:String
    lateinit var autoSp: SharedPreferences
    private lateinit var autoLoginPhone: String
    private lateinit var autoLoginPw: String
    private var autoLoginCheckBox by Delegates.notNull<Boolean>()

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 로그인한 유저 정보 저장
        loginSp = getSharedPreferences("loginInfo", Context.MODE_PRIVATE)
        memId = loginSp.getString("mem_id", "").toString()
        memName = loginSp.getString("mem_name","").toString()

        // 자동로그인 정보 저장
        autoSp = getSharedPreferences("autoLoginInfo", Context.MODE_PRIVATE)
        autoLoginPhone = autoSp.getString("loginPhone", "").toString()
        autoLoginPw = autoSp.getString("loginPw", "").toString()
        autoLoginCheckBox = autoSp.getBoolean("loginCb", false)

        // 화면 클릭시 키보드 내리기
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

    // 화면 클릭시 키보드 내리기
    private fun hideKeyboard() {
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
                            val editorAuto = autoSp.edit()
                            if (binding.loginCbAutoLogin.isChecked) {
                                editorAuto.putString("loginPhone", phone)
                                editorAuto.putString("loginPw", pw)
                                editorAuto.putBoolean("loginCb", true)
                                editorAuto.apply()
                            }
                            val editorMem = loginSp.edit()
                            val memberInfo = Gson().fromJson(res, Member::class.java)
                            editorMem.putString("mem_id", memberInfo.mem_id)
                            editorMem.putString("mem_name", memberInfo.mem_name)
                            editorMem.apply()

                            val intent = Intent(this@LoginActivity, MainActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                    }

                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                        t.localizedMessage?.let { Log.d("login", it) }
                    }
                })
            }
        }
    }
}