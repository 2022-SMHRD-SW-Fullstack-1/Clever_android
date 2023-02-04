package com.example.clever.view

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import com.example.clever.R
import com.example.clever.adapter.MainAdapter
import com.example.clever.databinding.ActivityMainBinding
import com.example.clever.decorator.other.MainRvDecorator
import com.example.clever.model.GroupVO
import com.example.clever.model.Member
import com.example.clever.retrofit.RetrofitClient
import com.example.clever.view.profile.ProfileActivity
import okhttp3.ResponseBody
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
        binding.mainRv.layoutManager = GridLayoutManager(this@MainActivity, 1)
        binding.mainRv.addItemDecoration(MainRvDecorator(16))

        // Event 처리
        binding.mainBtnJoinGroup.setOnClickListener {
            joinGroup()
        }

        binding.mainImgProfile.setOnClickListener {
            val intent = Intent(this@MainActivity, ProfileActivity::class.java)
            startActivity(intent)
        }

    }

    override fun onResume() {
        super.onResume()
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
        binding.mainRv.layoutManager = GridLayoutManager(this@MainActivity, 1)
        binding.mainRv.addItemDecoration(MainRvDecorator(16))

        // Event 처리
        binding.mainBtnJoinGroup.setOnClickListener {
            joinGroup()
        }

        binding.mainImgProfile.setOnClickListener {
            val intent = Intent(this@MainActivity, ProfileActivity::class.java)
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
    private fun joinGroup(){
        val view = layoutInflater.inflate(R.layout.custom_dialog_alert_input, null)
        val dialog = AlertDialog.Builder(this@MainActivity, R.style.CustomAlertDialog).setView(view).create()

        val alertInputTvTitle = view.findViewById<TextView>(R.id.alertInputTvTitle)
        val alertInputEt = view.findViewById<EditText>(R.id.alertInputEt)
        val alertInputBtnCancel = view.findViewById<Button>(R.id.alertInputBtnCancel)
        val alertInputBtnAccept = view.findViewById<TextView>(R.id.alertInputBtnAccept)

        alertInputTvTitle.text = "코드입력"

        alertInputBtnAccept.setOnClickListener {
            val group_serial = alertInputEt.text.toString()
            val req = GroupVO(null, null, null, group_serial, memId, null, null)
            RetrofitClient.api.joinGroup(req).enqueue(object : Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    val res = response.body()?.string()
                    if(res.toString() == "1"){
                        Toast.makeText(this@MainActivity, "그룹 추가 되었습니다.", Toast.LENGTH_SHORT).show()
                        getGroup()
                        dialog.dismiss()
                    }else{
                        Toast.makeText(this@MainActivity, "코드를 확인해주세요", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    TODO("Not yet implemented")
                }
            })
        }
        alertInputBtnCancel.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }
}