package com.example.clever.view

import android.app.AlertDialog
import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.clever.R
import com.example.clever.adapter.MainAdapter
import com.example.clever.databinding.ActivityMainBinding
import com.example.clever.model.GroupVO

private lateinit var binding: ActivityMainBinding

class MainActivity : AppCompatActivity() {

    // Item
    val groupList = ArrayList<GroupVO>()
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
//        getGroup()

        // Adapter
        adapter = MainAdapter(
            this@MainActivity,
            groupList
        )

        // Adapter Container 연결
        binding.mainRv.adapter = adapter
        binding.mainRv.layoutManager = LinearLayoutManager(this@MainActivity)

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
    }

    // 참여중이 group 가져오기
//    private fun getGroup(){
//        RetrofitClient.api.getGroup().enqueue(object :Callback<ResponseBody>{
//            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
//                TODO("Not yet implemented")
//            }
//
//            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
//                TODO("Not yet implemented")
//            }
//        })
//    }

    // 그룹 참여하기
}