package com.example.clever.view

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.telephony.PhoneNumberFormattingTextWatcher
import android.text.Editable
import android.text.TextWatcher
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

        binding.joinCl.setOnTouchListener { _, _ ->
            hideKeyboard()
            false
        }

        binding.joinClGoLogin.setOnClickListener {
            finish()
        }

        binding.joinBtnJoin.setOnClickListener {
            joinMember()

        }
    }

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

    private fun joinMember() {
        val phone = binding.joinEtPhone.text.toString().trim()
        val name = binding.joinEtName.text.toString().trim()
        val pw = binding.joinEtPw.text.toString().trim()
        val pwRe = binding.joinEtPwRe.text.toString().trim()
        val email = binding.joinEtEmail.text.toString().trim()

        if (phone == "") {
            Toast.makeText(this@JoinActivity, "?????????????????? ??????????????????", Toast.LENGTH_SHORT).show()
        } else {
            if (name == "") {
                Toast.makeText(this@JoinActivity, "????????? ??????????????????", Toast.LENGTH_SHORT).show()
            } else {
                if (pw == "") {
                    Toast.makeText(this@JoinActivity, "??????????????? ??????????????????", Toast.LENGTH_SHORT).show()
                } else {
                    if (pwRe == "") {
                        Toast.makeText(
                            this@JoinActivity,
                            "???????????? ???????????? ??????????????????",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        if (pw != pwRe) {
                            Toast.makeText(
                                this@JoinActivity,
                                "??????????????? ???????????? ????????????.",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            if (email == "") {
                                Toast.makeText(
                                    this@JoinActivity,
                                    "???????????? ??????????????????",
                                    Toast.LENGTH_SHORT
                                )
                                    .show()
                            } else {
                                val joinInfo = Member(phone, pw, name, email)

                                RetrofitClient.api.joinMember(joinInfo)
                                    .enqueue(object : Callback<ResponseBody> {
                                        override fun onResponse(
                                            call: Call<ResponseBody>,
                                            response: Response<ResponseBody>
                                        ) {
                                            if (response.body()?.string() == "1") {
                                                Toast.makeText(
                                                    this@JoinActivity,
                                                    "??????????????? ?????????????????????.",
                                                    Toast.LENGTH_SHORT
                                                )
                                                    .show()
                                                finish()
                                            } else {
                                                Toast.makeText(
                                                    this@JoinActivity,
                                                    "??????????????? ?????????????????????.",
                                                    Toast.LENGTH_SHORT
                                                )
                                                    .show()
                                            }
                                        }

                                        override fun onFailure(
                                            call: Call<ResponseBody>,
                                            t: Throwable
                                        ) {
                                            t.localizedMessage?.let { Log.d("join", it) }
                                        }
                                    })
                            }
                        }
                    }
                }
            }
        }
    }
}