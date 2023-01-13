package com.example.clever.view

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.clever.R
import com.example.clever.databinding.ActivityJoinBinding
import com.example.clever.model.Member
import com.example.clever.retrofit.RetrofitClient
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class JoinActivity : AppCompatActivity() {

    private lateinit var binding: ActivityJoinBinding

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_join)

        binding = ActivityJoinBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.joinCl.setOnTouchListener(object : OnTouchListener {
            override fun onTouch(p0: View?, p1: MotionEvent?): Boolean {
                hideKeyboard()
                return false
            }
        })

        binding.joinBtnJoin.setOnClickListener {
            finish()
        }

        binding.joinClGoLogin.setOnClickListener {
            finish()
        }

        binding.joinBtnJoin.setOnClickListener {
            join()
        }
    }

    fun hideKeyboard() {
        val inputManager: InputMethodManager =
            this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(
            this.currentFocus!!.windowToken,
            InputMethodManager.HIDE_NOT_ALWAYS
        )
    }

    private fun join() {
        val phone = binding.joinEtPhone.text.toString()
        val name = binding.joinEtName.text.toString()
        val pw = binding.joinEtPw.text.toString()
        val pwRe = binding.joinEtPwRe.text.toString()
        val email = binding.joinEtEmail.text.toString()

        val joinInfo = Member(phone, name, pw, email, null)

        RetrofitClient.api.join(joinInfo).enqueue(object : Callback<ResponseBody>{
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                Toast.makeText(this@JoinActivity, response.body()?.string(), Toast.LENGTH_SHORT).show()
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.d("hello", t.localizedMessage)
            }
        })
    }
}