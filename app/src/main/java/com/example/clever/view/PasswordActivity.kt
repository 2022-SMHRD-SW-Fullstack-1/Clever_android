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
import com.example.clever.R
import com.example.clever.databinding.ActivityPasswordBinding
import com.example.clever.model.Member
import com.example.clever.retrofit.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PasswordActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPasswordBinding

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

        binding.pwBtnPwChange.isEnabled = false
        binding.pwBtnPwChange.setOnClickListener {
            val intent = Intent(this@PasswordActivity, PasswordChangeActivity::class.java)
            startActivity(intent)
        }

        binding.pwBtnCode.setOnClickListener {
            getCode()
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

    private fun getCode(){
        val id = binding.pwEtPhone.text.toString()
        val email = binding.pwEtEmail.text.toString()
        val code = binding.pwEtCode.text.toString()

        val req = Member(id, null, email)
        RetrofitClient.api.getCode(req).enqueue(object : Callback<String>{
            override fun onResponse(call: Call<String>, response: Response<String>) {
                val res = response.body()
                Log.d("코드받기", res.toString())
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })
    }
}