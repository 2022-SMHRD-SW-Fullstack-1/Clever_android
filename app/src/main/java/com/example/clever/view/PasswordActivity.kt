package com.example.clever.view

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.example.clever.R
import com.example.clever.databinding.ActivityPasswordBinding
import com.example.clever.model.Member
import com.example.clever.retrofit.RetrofitClient
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PasswordActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPasswordBinding

    lateinit var etCode: String
    lateinit var code: String
    lateinit var id: String
    lateinit var email: String

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_password)

        binding = ActivityPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.pwCl.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(p0: View?, p1: MotionEvent?): Boolean {
                hideKeyboard()
                return false
            }
        })

        binding.pwBtnCode.setOnClickListener {
            getCode()
        }

        binding.pwBtnPwChange.isEnabled = false
        binding.pwBtnPwChange.setOnClickListener {
            certification()
        }


        binding.pwClGoLogin.setOnClickListener {
            finish()
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

    private fun getCode() {
        id = binding.pwEtPhone.text.toString()
        email = binding.pwEtEmail.text.toString()

        val req = Member(id, null, email)
        RetrofitClient.api.getCode(req).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                val res = response.body()?.string()
                if (res != "1") {
                    Toast.makeText(this@PasswordActivity, "이메일로 인증번호를 발송했습니다.", Toast.LENGTH_SHORT)
                        .show()
                    code = res.toString()
                    binding.pwBtnPwChange.isEnabled = true
                } else {
                    Toast.makeText(
                        this@PasswordActivity,
                        "휴대폰번호 또는 이메일을 다시 확인해주세요.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                t.localizedMessage?.let { Log.d("코드받기", it) }
            }
        })
    }

    private fun certification() {
        etCode = binding.pwEtCode.text.toString()

        if (code == etCode) {
            val intent = Intent(this@PasswordActivity, PasswordChangeActivity::class.java)
            intent.putExtra("mem_id", id)
            startActivity(intent)
            finish()
        } else {
            Toast.makeText(this@PasswordActivity, "인증번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show()
        }
    }
}