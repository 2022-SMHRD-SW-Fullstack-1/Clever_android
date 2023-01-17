package com.example.clever.adapter

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.clever.R
import com.example.clever.model.GroupVO
import com.example.clever.retrofit.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MoreMemberAdapter(val context: Context, private val memberList: ArrayList<GroupVO>) :
    RecyclerView.Adapter<MoreMemberAdapter.ViewHolder>() {

    lateinit var loginSp: SharedPreferences
    private lateinit var memId: String

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val moreMemRvPic: ImageView
        val moreMemRvName: TextView
        val moreMemRvDate: TextView
        val moreMemRvOut: ImageView

        init {
            loginSp = context.getSharedPreferences("loginInfo", Context.MODE_PRIVATE)
            memId = loginSp.getString("mem_id", "").toString()

            moreMemRvPic = itemView.findViewById(R.id.moreMemRvPic)
            moreMemRvName = itemView.findViewById(R.id.moreMemRvName)
            moreMemRvDate = itemView.findViewById(R.id.moreMemRvDate)
            moreMemRvOut = itemView.findViewById(R.id.moreMemRvOut)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(context)
        val view = layoutInflater.inflate(R.layout.template_more_mem_rv, null)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val date = memberList[position].join_dt
        val year = date?.substring(0, 4)
        val month = date?.substring(5, 7)
        val day = date?.substring(8, 10)
        holder.moreMemRvDate.text = "${year}.${month}.${day} 합류"

        holder.moreMemRvName.text = memberList[position].mem_name

        val req = GroupVO(memberList[position].group_seq)
        RetrofitClient.api.groupInfo(req).enqueue(object : Callback<GroupVO>{
            override fun onResponse(call: Call<GroupVO>, response: Response<GroupVO>) {
                val res = response.body()!!
                if(res.mem_id == memId) holder.moreMemRvOut.isVisible = true
            }

            override fun onFailure(call: Call<GroupVO>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })

        holder.moreMemRvOut.setOnClickListener {

        }

        holder.moreMemRvOut.setOnClickListener {
            val builder = AlertDialog.Builder(it.context)
            builder.setTitle("코드입력")
                .setMessage("안녕")
                .setPositiveButton("확인",
                    DialogInterface.OnClickListener { dialog, id ->

                    })
                .setNegativeButton("취소",
                    DialogInterface.OnClickListener { dialog, id ->
                        null
                    }).create()
            builder.show()
        }

    }

    override fun getItemCount(): Int {
        return memberList.size
    }

}