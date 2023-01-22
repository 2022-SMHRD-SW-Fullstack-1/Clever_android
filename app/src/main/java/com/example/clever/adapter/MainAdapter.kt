package com.example.clever.adapter

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.clever.R
import com.example.clever.model.GroupVO
import com.example.clever.retrofit.RetrofitClient
import com.example.clever.view.home.HomeActivity
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainAdapter(val context: Context, private val groupList: ArrayList<GroupVO>) :
    RecyclerView.Adapter<MainAdapter.ViewHolder>() {

    lateinit var loginSp: SharedPreferences
    private lateinit var memId: String

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

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

    @SuppressLint("MissingInflatedId")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvGroupName.text = groupList[position].group_name

        holder.tvGroupName.setOnClickListener {
            val intent = Intent(context, HomeActivity::class.java)
            intent.putExtra("group_seq", "${groupList[position].group_seq}")
            intent.putExtra("group_name", groupList[position].group_name)
            context.startActivity(intent)
        }

        holder.imgMore.setOnClickListener {
            val layoutInflater = LayoutInflater.from(context)
            val view = layoutInflater.inflate(R.layout.custom_dialog_alert, null)

            val alertDialog = AlertDialog.Builder(context, R.style.CustomAlertDialog)
                .setView(view).create()

            val alertBtnCancel = view.findViewById<ImageButton>(R.id.alertBtnCancel)
            val alertTvTitle = view.findViewById<TextView>(R.id.alertTvTitle)
            val alertTvContent = view.findViewById<TextView>(R.id.alertTvContent)
            val alertBtnOk = view.findViewById<Button>(R.id.alertBtnOk)

            alertTvTitle.text = "그룹나가기"
            alertTvContent.text = "${groupList[position].group_name} 을/를 나가시겠습니까 ?"

            alertBtnOk.setOnClickListener {
                loginSp = context.getSharedPreferences("loginInfo", Context.MODE_PRIVATE)
                memId = loginSp.getString("mem_id", "").toString()
                val group_seq = groupList[position].group_seq

                val req = GroupVO(group_seq, memId)
                RetrofitClient.api.groupOut(req).enqueue(object : Callback<ResponseBody>{
                    override fun onResponse(
                        call: Call<ResponseBody>,
                        response: Response<ResponseBody>
                    ) {
                        val res = response.body()?.string()
                        Log.d("groupOut", res.toString())
                        if(res.toString() == "1"){
                            Toast.makeText(context, "${groupList[position].group_name} 을/를 나갔습니다.", Toast.LENGTH_SHORT).show()
                            alertDialog.dismiss()
                        }
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
        return groupList.size
    }
}