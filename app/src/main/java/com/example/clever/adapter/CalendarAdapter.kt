package com.example.clever.adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.clever.R
import com.example.clever.model.AttendanceVO
import com.example.clever.model.ChangeAttendanceVO
import com.example.clever.retrofit.RetrofitClient
import com.example.clever.view.home.cal.AttChangeActivity
import com.google.gson.Gson
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class CalendarAdapter(val context: Context, val selectedList: ArrayList<AttendanceVO>) :
    RecyclerView.Adapter<CalendarAdapter.ViewHolder>() {
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val calendarRvTvAtt: TextView
        val calendarRvBtnChange: Button
        val calendarRvBtnIng: Button
        val calendarRvBtnReject: Button
        val rejectCl: ConstraintLayout
        val calendarRvBtnReChange: Button
        val calendarRvBtnMemo: Button
        val calendarRvTvMemo: TextView

        init {
            calendarRvTvAtt = itemView.findViewById(R.id.calendarRvTvAtt)
            calendarRvBtnChange = itemView.findViewById(R.id.calendarRvBtnChange)
            calendarRvBtnIng = itemView.findViewById(R.id.calendarRvBtnIng)
            calendarRvBtnReject = itemView.findViewById(R.id.calendarRvBtnReject)
            rejectCl = itemView.findViewById(R.id.rejectCl)
            calendarRvBtnReChange = itemView.findViewById(R.id.calendarRvBtnReChange)
            calendarRvBtnMemo = itemView.findViewById(R.id.calendarRvBtnMemo)
            calendarRvTvMemo = itemView.findViewById(R.id.calendarRvTvMemo)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(context)
        val view = layoutInflater.inflate(R.layout.template_cal_rv, null)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.calendarRvTvMemo.visibility = View.GONE
        var start = selectedList[position].att_sche_start_time.toString()
        var end = selectedList[position].att_sche_end_time.toString()
        start = start.substring(0, 5)
        end = end.substring(0, 5)
        val time = "$start ~ $end"
        holder.calendarRvTvAtt.text = time

        val req = ChangeAttendanceVO(selectedList[position].att_seq!!.toInt())
        RetrofitClient.api.checkAttCh(req).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(
                call: Call<ResponseBody>,
                response: Response<ResponseBody>
            ) {
                val res = response.body()?.string()
                val chAttInfo = Gson().fromJson(res, ChangeAttendanceVO::class.java)
                Log.d("calendar", res.toString())
                if (res.toString() != "") {
                    holder.calendarRvTvMemo.text = chAttInfo.ch_reject_memo.toString()
                    when (chAttInfo.ch_approve) {
                        "N" -> {
                            holder.calendarRvBtnReject.visibility = View.VISIBLE
                            holder.calendarRvBtnIng.visibility = View.GONE
                            holder.calendarRvBtnChange.visibility = View.GONE
                        }
                        "Y" -> {
                            holder.calendarRvBtnReject.visibility = View.GONE
                            holder.calendarRvBtnIng.visibility = View.GONE
                            holder.calendarRvBtnChange.visibility = View.VISIBLE
                        }
                        else -> {
                            holder.calendarRvBtnReject.visibility = View.GONE
                            holder.calendarRvBtnIng.visibility = View.VISIBLE
                            holder.calendarRvBtnChange.visibility = View.GONE
                        }
                    }
                } else {
                    holder.calendarRvBtnReject.visibility = View.GONE
                    holder.calendarRvBtnIng.visibility = View.GONE
                    holder.calendarRvBtnChange.visibility = View.VISIBLE
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                t.localizedMessage?.let { Log.d("calendar", it) }
            }
        })

        holder.calendarRvBtnChange.setOnClickListener {
            val intent = Intent(context, AttChangeActivity::class.java)
            intent.putExtra("att_seq", "${selectedList[position].att_seq}")
            intent.putExtra(
                "start_time",
                selectedList[position].att_sche_start_time!!.substring(0, 5)
            )
            intent.putExtra("end_time", selectedList[position].att_sche_end_time!!.substring(0, 5))
            context.startActivity(intent)
        }

        holder.calendarRvBtnReject.setOnClickListener {
            holder.rejectCl.visibility = View.VISIBLE
        }

        holder.calendarRvBtnReChange.setOnClickListener {
            holder.rejectCl.visibility = View.GONE
            val intent = Intent(context, AttChangeActivity::class.java)
            intent.putExtra("att_seq", "${selectedList[position].att_seq}")
            intent.putExtra(
                "start_time",
                selectedList[position].att_sche_start_time!!.substring(0, 5)
            )
            intent.putExtra("end_time", selectedList[position].att_sche_end_time!!.substring(0, 5))
            context.startActivity(intent)
        }

        holder.calendarRvBtnMemo.setOnClickListener {
            holder.rejectCl.visibility = View.GONE
            holder.calendarRvTvMemo.visibility = View.VISIBLE
        }
    }

    override fun getItemCount(): Int {
        return selectedList.size
    }
}