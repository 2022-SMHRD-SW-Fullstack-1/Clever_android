package com.example.clever.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.clever.R
import com.example.clever.model.AttendanceVO
import com.example.clever.view.home.cal.AttChangeActivity

class CalendarAdapter(val context: Context, val selectedList: ArrayList<AttendanceVO>) :
    RecyclerView.Adapter<CalendarAdapter.ViewHolder>() {
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val calendarRvTvAtt: TextView
        val calendarRvBtnChange: Button

        init {
            calendarRvTvAtt = itemView.findViewById(R.id.calendarRvTvAtt)
            calendarRvBtnChange = itemView.findViewById(R.id.calendarRvBtnChange)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(context)
        val view = layoutInflater.inflate(R.layout.template_cal_rv, null)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var start = selectedList[position].att_sche_start_time.toString()
        var end = selectedList[position].att_sche_end_time.toString()
        start = start.substring(0, 5)
        end = end.substring(0, 5)
        val time = "$start ~ $end"
        holder.calendarRvTvAtt.text = time

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
    }

    override fun getItemCount(): Int {
        return selectedList.size
    }
}