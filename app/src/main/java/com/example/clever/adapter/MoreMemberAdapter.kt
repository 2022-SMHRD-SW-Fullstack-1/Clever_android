package com.example.clever.adapter

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.clever.R
import com.example.clever.model.GroupVO
import com.example.clever.retrofit.RetrofitClient
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MoreMemberAdapter(val context: Context, private val memberList: ArrayList<GroupVO>) :
    RecyclerView.Adapter<MoreMemberAdapter.ViewHolder>() {

    lateinit var loginSp: SharedPreferences
    private lateinit var memId: String

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val moreMemRvName: TextView
        val moreMemRvDate: TextView
        val moreMemRvOut: ImageView

        init {
            loginSp = context.getSharedPreferences("loginInfo", Context.MODE_PRIVATE)
            memId = loginSp.getString("mem_id", "").toString()

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
        RetrofitClient.api.groupInfo(req).enqueue(object : Callback<GroupVO> {
            override fun onResponse(call: Call<GroupVO>, response: Response<GroupVO>) {
                val res = response.body()!!
                if (res.mem_id == memId) holder.moreMemRvOut.isVisible = true
            }

            override fun onFailure(call: Call<GroupVO>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })

        holder.moreMemRvOut.setOnClickListener {
            val layoutInflater = LayoutInflater.from(context)
            val view = layoutInflater.inflate(R.layout.custom_dialog_alert, null)

            val alertDialog = AlertDialog.Builder(context, R.style.CustomAlertDialog)
                .setView(view).create()

            val alertBtnCancel = view.findViewById<ImageButton>(R.id.alertBtnCancel)
            val alertTvTitle = view.findViewById<TextView>(R.id.alertTvTitle)
            val alertTvContent = view.findViewById<TextView>(R.id.alertTvContent)
            val alertBtnOk = view.findViewById<Button>(R.id.alertBtnOk)

            alertTvTitle.text = "추방하기"
            alertTvContent.text = "${memberList[position].mem_name} 을/를 추방하시겠습니까 ?"

            alertBtnOk.setOnClickListener {
                val req = GroupVO(memberList[position].group_seq, memberList[position].mem_id.toString())
                RetrofitClient.api.groupOut(req).enqueue(object : Callback<ResponseBody> {
                    override fun onResponse(
                        call: Call<ResponseBody>,
                        response: Response<ResponseBody>
                    ) {
                        val res = response.body()?.string()
                    }

                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                        TODO("Not yet implemented")
                    }
                })
            }
            alertBtnCancel.setOnClickListener {
                alertDialog.dismiss()
            }
            alertDialog.show()
        }
    }

    override fun getItemCount(): Int {
        return memberList.size
    }

}