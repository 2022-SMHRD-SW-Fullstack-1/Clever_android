package com.example.clever.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.clever.R
import com.example.clever.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.loginBtnLogin.setOnClickListener {
            val intent = Intent(this@LoginActivity, MainActivity::class.java)
            startActivity(intent)
        }

        binding.loginTvJoin.setOnClickListener{
            val intent = Intent(this@LoginActivity, JoinActivity::class.java)
            startActivity(intent)
        }

        binding.loginClGoPw.setOnClickListener{
            val intent = Intent(this@LoginActivity, PasswordActivity::class.java)
            startActivity(intent)
        }
    }
}