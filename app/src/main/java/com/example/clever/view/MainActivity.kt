package com.example.clever.view

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.clever.R
import com.example.clever.adapter.MainAdapter
import com.example.clever.databinding.ActivityMainBinding
import com.example.clever.decorator.main.MainRvDecorator
import com.example.clever.model.GroupVO
import com.example.clever.model.Member
import com.example.clever.retrofit.RetrofitClient
import com.example.clever.view.home.HomeActivity
import com.example.clever.view.profile.ProfileActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    lateinit var loginSp: SharedPreferences
    private lateinit var memId: String

    // Item
    private val groupList = ArrayList<GroupVO>()

    // Adapter
    lateinit var adapter: MainAdapter

    // 그룹참여 코드
    lateinit var groupCode: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Container
        // binding.mainRv

        // Template
        // template_main_rv

        // Item
        getGroup()

        // Adapter
        adapter = MainAdapter(
            this@MainActivity,
            groupList
        )

        // Adapter Container 연결
        binding.mainRv.adapter = adapter
        binding.mainRv.layoutManager = LinearLayoutManager(this@MainActivity)
        binding.mainRv.addItemDecoration(MainRvDecorator(16))

        // Event 처리
        binding.mainBtnJoinGroup.setOnClickListener {
            val etDialog = EditText(this@MainActivity)
            val builder = AlertDialog.Builder(it.context)
            builder.setTitle("코드입력")
                .setView(etDialog)
                .setPositiveButton("확인",
                    DialogInterface.OnClickListener { dialog, id ->
                        groupCode = etDialog.text.toString()
                    })
                .setNegativeButton("취소",
                    DialogInterface.OnClickListener { dialog, id ->
                        null
                    }).create()
            builder.show()
        }

        binding.mainImgProfile.setOnClickListener {
            val intent = Intent(this@MainActivity, ProfileActivity::class.java)
            startActivity(intent)
        }

        binding.mainTvGoHome.setOnClickListener {
            val intent = Intent(this@MainActivity, HomeActivity::class.java)
            startActivity(intent)
        }
    }

    // 참여중이 group 가져오기
    private fun getGroup() {
        loginSp = getSharedPreferences("loginInfo", Context.MODE_PRIVATE)
        memId = loginSp.getString("mem_id", "").toString()

        RetrofitClient.api.getGroup(Member(memId)).enqueue(object : Callback<List<GroupVO>> {
            override fun onResponse(call: Call<List<GroupVO>>, response: Response<List<GroupVO>>) {
                val res = response.body()
                groupList.clear()
                for (i in 0 until res!!.size) {
                    groupList.add(res[i])
                }
                binding.mainTvCount.text = groupList.size.toString()

                adapter.notifyDataSetChanged()
            }

            override fun onFailure(call: Call<List<GroupVO>>, t: Throwable) {
                t.localizedMessage?.let { Log.d("group", it) }
            }
        })
    }

    // 그룹 참여하기
}