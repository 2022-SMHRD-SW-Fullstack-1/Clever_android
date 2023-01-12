package com.example.clever.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.clever.R
import com.example.clever.databinding.ActivityJoinBinding
import com.example.clever.model.Member
import com.example.clever.retrofit.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.Body


class JoinActivity : AppCompatActivity() {

    private lateinit var binding: ActivityJoinBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_join)

        binding = ActivityJoinBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.joinBtnJoin.setOnClickListener {
            val intent = Intent(this@JoinActivity, LoginActivity::class.java)
            startActivity(intent)
        }

        binding.joinClGoLogin.setOnClickListener {
            val intent = Intent(this@JoinActivity, LoginActivity::class.java)
            startActivity(intent)
        }

        binding.joinBtnJoin.setOnClickListener {
            val name = binding.joinEtName.text.toString()
            val phone = binding.joinEtPhone.text.toString()
            val pw = binding.joinEtPw.text.toString()
            val pwRe = binding.joinEtPwRe.text.toString()

            RetrofitClient.api.joinMember(name, phone, pw).enqueue(object : Callback<Member>{
                override fun onResponse(call: Call<Member>, response: Response<Member>) {
                    val result = response.body()
                    Log.d("로그인", "${result}")
                }

                override fun onFailure(call: Call<Member>, t: Throwable) {
                    Log.d("로그인", "${t.localizedMessage}")
                }
            })
        }
    }
}