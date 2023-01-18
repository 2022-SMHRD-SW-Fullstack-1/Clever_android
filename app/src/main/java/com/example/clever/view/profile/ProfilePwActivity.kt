package com.example.clever.view.profile

import android.annotation.SuppressLint
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.example.clever.R
import com.example.clever.databinding.ActivityProfilePwBinding

class ProfilePwActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfilePwBinding

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_pw)

        binding = ActivityProfilePwBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.profilePwCl.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(p0: View?, p1: MotionEvent?): Boolean {
                hideKeyboard()
                return false
            }
        })

        binding.profilePwImgBack.setOnClickListener {
            finish()
        }

        binding.profilePwBtnPwCh.setOnClickListener {
            finish()
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
}