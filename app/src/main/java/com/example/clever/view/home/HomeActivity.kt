package com.example.clever.view.home

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.clever.R
import com.example.clever.databinding.ActivityHomeBinding
import com.example.clever.view.home.cal.CalendarFragment
import com.example.clever.view.home.more.MoreFragment
import com.example.clever.view.home.notice.NoticeFragment
import com.example.clever.view.home.todo.TodoFragment
import com.example.clever.view.profile.ProfileActivity


class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportFragmentManager.beginTransaction().replace(
            R.id.fl,
            TodoFragment()
        ).commit()

        binding.bnv.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.tabHome1 -> {
                    supportFragmentManager.beginTransaction().replace(
                        R.id.fl,
                        TodoFragment()
                    ).commit()
                }
                R.id.tabHome2 -> {
                    supportFragmentManager.beginTransaction().replace(
                        R.id.fl,
                        NoticeFragment()
                    ).commit()
                }
                R.id.tabHome3 -> {
                    supportFragmentManager.beginTransaction().replace(
                        R.id.fl,
                        CalendarFragment()
                    ).commit()
                }
                R.id.tabHome4 -> {
                    supportFragmentManager.beginTransaction().replace(
                        R.id.fl,
                        MoreFragment()
                    ).commit()
                }
            }
            true
        }

        binding.homeImgBack.setOnClickListener {
            finish()
        }

        binding.homeImgProfile.setOnClickListener {
            val intent = Intent(this@HomeActivity, ProfileActivity::class.java)
            startActivity(intent)
        }
    }
}