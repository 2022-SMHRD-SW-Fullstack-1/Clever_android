package com.example.clever.view.home.more

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.GridLayoutManager
import com.example.clever.R
import com.example.clever.adapter.MoreMemberAdapter
import com.example.clever.databinding.ActivityMoreMemberBinding
import com.example.clever.model.GroupVO
import com.example.clever.model.Member
import com.example.clever.retrofit.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MoreMemberActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMoreMemberBinding

    lateinit var group_seq: String

    // item
    var memberList = ArrayList<GroupVO>()

    // Adapter
    lateinit var adapter: MoreMemberAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_more_member)

        group_seq = intent.getStringExtra("group_seq").toString()

        binding = ActivityMoreMemberBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // container
        // moreMemberRv

        // template
        // template_more_mem_rv

        // item
        groupInfo()

        // adapter
        adapter = MoreMemberAdapter(
            this@MoreMemberActivity,
            memberList
        )

        // adapter, container 연결
        binding.moreMemberRv.adapter = adapter
        binding.moreMemberRv.layoutManager = GridLayoutManager(this@MoreMemberActivity, 1)

        binding.moreMemImgBack.setOnClickListener {
            finish()
        }
    }

    private fun groupInfo() {
        val groupInfo = GroupVO(group_seq.toInt())

        RetrofitClient.api.groupInfo(groupInfo).enqueue(object : Callback<GroupVO> {
            override fun onResponse(call: Call<GroupVO>, response: Response<GroupVO>) {
                val res = response.body()
                val memId = res!!.mem_id.toString()
                leaderInfo(memId)

                val resultDt = res!!.group_dt.toString()
                val year = resultDt.substring(0, 4)
                val month = resultDt.substring(5, 7)
                val day = resultDt.substring(8, 10)
                binding.moreMemTvDate.text = "${year}.${month}.${day} 생성"
            }

            override fun onFailure(call: Call<GroupVO>, t: Throwable) {
                t.localizedMessage?.let { Log.d("more fragment", it) }
            }
        })
    }

    private fun leaderInfo(memId: String) {
        RetrofitClient.api.userInfo(Member(memId)).enqueue(object : Callback<Member> {
            override fun onResponse(call: Call<Member>, response: Response<Member>) {
                val res = response.body()
                binding.moreMemTvName.text = res!!.mem_name
                groupMem(res!!.mem_id)
            }

            override fun onFailure(call: Call<Member>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })
    }

    private fun groupMem(mem_id: String) {
        val groupInfo = GroupVO(group_seq.toInt())

        RetrofitClient.api.groupMem(groupInfo).enqueue(object : Callback<List<GroupVO>> {
            override fun onResponse(call: Call<List<GroupVO>>, response: Response<List<GroupVO>>) {
                val res = response.body()
                binding.moreMemTvCount.text = ((res!!.size)-1).toString()

                for (i in 0 until res!!.size) {
                    if (mem_id != res[i].mem_id) memberList.add(res[i])
                }
                adapter.notifyDataSetChanged()
            }

            override fun onFailure(call: Call<List<GroupVO>>, t: Throwable) {
                t.localizedMessage?.let { Log.d("more fragment", it) }
            }
        })
    }
}