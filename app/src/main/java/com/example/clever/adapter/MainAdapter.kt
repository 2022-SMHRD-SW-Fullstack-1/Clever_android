package com.example.clever.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.clever.R
import com.example.clever.model.GroupVO

class MainAdapter(val context: Context, val groupList: ArrayList<GroupVO>): RecyclerView.Adapter<MainAdapter.ViewHolder>() {
    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        val imgMore: ImageView
        val imgState: ImageView
        val tvGroupName: TextView

        init {
            imgMore = itemView.findViewById(R.id.imgMore)
            imgState = itemView.findViewById(R.id.imgState)
            tvGroupName = itemView.findViewById(R.id.tvGroupName)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(context)
        val view = layoutInflater.inflate(R.layout.template_main_rv, null)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        return groupList.size
    }
}