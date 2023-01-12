package com.example.clever.view

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import com.example.clever.R
import com.example.clever.databinding.ActivityJoinBinding


class JoinActivity : AppCompatActivity() {

    private lateinit var binding: ActivityJoinBinding

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_join)

        binding = ActivityJoinBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.joinCl.setOnTouchListener(object : OnTouchListener{
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

//        binding.joinBtnJoin.setOnClickListener {
//            val name = binding.joinEtName.text.toString()
//            val phone = binding.joinEtPhone.text.toString()
//            val pw = binding.joinEtPw.text.toString()
//            val pwRe = binding.joinEtPwRe.text.toString()
//
//            RetrofitClient.api.joinMember(name, phone, pw).enqueue(object : Callback<Member>{
//                override fun onResponse(call: Call<Member>, response: Response<Member>) {
//                    val result = response.body()
//                    Log.d("로그인", "${result}")
//                }
//
//                override fun onFailure(call: Call<Member>, t: Throwable) {
//                    Log.d("로그인", "${t.localizedMessage}")
//                }
//            })
//        }
    }

    fun hideKeyboard() {
        val inputManager: InputMethodManager =
            this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(
            this.currentFocus!!.windowToken,
            InputMethodManager.HIDE_NOT_ALWAYS
        )
    }
}