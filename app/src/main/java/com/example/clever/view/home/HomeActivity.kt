package com.example.clever.view.home

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.clever.R
import com.example.clever.databinding.ActivityHomeBinding
import com.example.clever.model.Member
import com.example.clever.retrofit.RetrofitClient
import com.example.clever.view.home.cal.CalendarFragment
import com.example.clever.view.home.more.MoreFragment
import com.example.clever.view.home.notice.NoticeFragment
import com.example.clever.view.home.todo.TodoFragment
import com.example.clever.view.profile.ProfileActivity
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.properties.Delegates


class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding

    lateinit var groupSp: SharedPreferences
    lateinit var group_seq : String
    lateinit var group_name : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val groupSeq = intent.getStringExtra("group_seq")
        val groupName = intent.getStringExtra("group_name")

        Log.d("home ac", groupSeq.toString())

        groupSp = getSharedPreferences("groupInfo", Context.MODE_PRIVATE)
        group_seq = groupSp.getString("group_seq", "").toString()
        group_name = groupSp.getString("group_name", "").toString()

        val editor = groupSp.edit()
        editor.putString("group_seq", groupSeq)
        editor.putString("group_name", groupName)
        editor.apply()

        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.homeTvGroupName.text = groupName

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