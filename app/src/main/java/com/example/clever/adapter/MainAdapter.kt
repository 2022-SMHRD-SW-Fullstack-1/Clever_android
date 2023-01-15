package com.example.clever.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.clever.R
import com.example.clever.model.GroupVO
import com.example.clever.view.home.HomeActivity

class MainAdapter(val context: Context, private val groupList: ArrayList<GroupVO>): RecyclerView.Adapter<MainAdapter.ViewHolder>() {
    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        val imgMore: ImageView
        val tvGroupName: TextView

        init {
            imgMore = itemView.findViewById(R.id.imgMore)
            tvGroupName = itemView.findViewById(R.id.tvGroupName)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(context)
        val view = layoutInflater.inflate(R.layout.template_main_rv, null)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvGroupName.text = groupList[position].group_name

        holder.tvGroupName.setOnClickListener {
            val intent = Intent(context, HomeActivity::class.java)
            intent.putExtra("group_seq", "${groupList[position].group_seq}")
            intent.putExtra("group_name", groupList[position].group_name)
            context.startActivity(intent)
        }

        holder.imgMore.setOnClickListener {

        }
    }

    override fun getItemCount(): Int {
        return groupList.size
    }
}