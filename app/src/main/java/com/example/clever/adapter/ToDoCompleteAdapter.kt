package com.example.clever.adapter

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.opengl.Visibility
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.clever.R
import com.example.clever.model.NoticeCommentVO
import com.example.clever.model.ToDoCompleteVO
import com.example.clever.retrofit.RetrofitClient
import com.example.clever.view.home.todo.TodoDetailActivity
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ToDoCompleteAdapter(val context: Context, val todoCmplList: ArrayList<ToDoCompleteVO>) :
    RecyclerView.Adapter<ToDoCompleteAdapter.ViewHolder>() {

    lateinit var loginSp: SharedPreferences
    private lateinit var memId: String

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val todoCplRvName: TextView
        val todoCplRvTvDate: TextView
        val todoCplRvImgPic: ImageView
        val todoCplRvClBg: ConstraintLayout
        val todoCplRvClPic: ConstraintLayout
        val todoCplRvBtnWrite: Button
        val todoCplRvMemo: TextView
        val todoCplRvMemoCl: ConstraintLayout
        val todoCplRvMemoImg: ImageView
        val todoCplRvMemoDelete: ImageView

        init {
            loginSp = context.getSharedPreferences("loginInfo", Context.MODE_PRIVATE)
            memId = loginSp.getString("mem_id", "").toString()

            todoCplRvName = itemView.findViewById(R.id.todoCplRvName)
            todoCplRvTvDate = itemView.findViewById(R.id.todoCplRvTvDate)
            todoCplRvImgPic = itemView.findViewById(R.id.todoCplRvImgPic)
            todoCplRvClBg = itemView.findViewById(R.id.todoCplRvClBg)
            todoCplRvClPic = itemView.findViewById(R.id.todoCplRvClPic)
            todoCplRvBtnWrite = itemView.findViewById(R.id.todoCplRvBtnWrite)
            todoCplRvMemo = itemView.findViewById(R.id.todoCplRvMemo)
            todoCplRvMemoCl = itemView.findViewById(R.id.todoCplRvMemoCl)
            todoCplRvMemoImg = itemView.findViewById(R.id.todoCplRvMemoImg)
            todoCplRvMemoDelete = itemView.findViewById(R.id.todoCplRvMemoDelete)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(context)
        val view = layoutInflater.inflate(R.layout.template_todo_cpl_rv, null)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.todoCplRvName.text = todoCmplList[position].mem_name

        val date = todoCmplList[position].cmpl_time!!
        val year = date.substring(0, 4)
        val month = date.substring(5, 7)
        val day = date.substring(8, 10)
        val hours = date.substring(11, 13)
        val min = date.substring(14, 16)
        holder.todoCplRvTvDate.text = "${year}.${month}.${day} ${hours}:${min}"

        if (todoCmplList[position].cmpl_memo == "" || todoCmplList[position].cmpl_memo == null){
            holder.todoCplRvMemoCl.visibility = View.GONE
            if(todoCmplList[position].mem_id == memId){
                holder.todoCplRvBtnWrite.visibility = View.VISIBLE

                holder.todoCplRvBtnWrite.setOnClickListener {
                    val intent = Intent(context, TodoDetailActivity::class.java)
                    intent.putExtra("todo_seq", todoCmplList[position].todo_seq.toString())
                    intent.putExtra("cate_seq", todoCmplList[position].cate_seq.toString())
                    intent.putExtra("cmpl_seq", todoCmplList[position].cmpl_seq.toString())
                    context.startActivity(intent)
                }
            }else{
                holder.todoCplRvBtnWrite.visibility = View.GONE
            }
        }else{
            holder.todoCplRvMemoCl.visibility = View.VISIBLE
            holder.todoCplRvBtnWrite.visibility = View.GONE
            if(todoCmplList[position].mem_id == memId){
                holder.todoCplRvMemoImg.visibility = View.VISIBLE
                holder.todoCplRvMemoDelete.visibility = View.VISIBLE

                holder.todoCplRvMemoImg.setOnClickListener {
                    val intent = Intent(context, TodoDetailActivity::class.java)
                    intent.putExtra("todo_seq", todoCmplList[position].todo_seq.toString())
                    intent.putExtra("cate_seq", todoCmplList[position].cate_seq.toString())
                    intent.putExtra("cmpl_seq", todoCmplList[position].cmpl_seq.toString())
                    context.startActivity(intent)
                }

                holder.todoCplRvMemoDelete.setOnClickListener {
                    val layoutInflater = LayoutInflater.from(context)
                    val view = layoutInflater.inflate(R.layout.custom_dialog_alert, null)

                    val alertDialog = AlertDialog.Builder(context, R.style.CustomAlertDialog)
                        .setView(view).create()

                    val alertBtnCancel = view.findViewById<ImageButton>(R.id.alertBtnCancel)
                    val alertTvTitle = view.findViewById<TextView>(R.id.alertTvTitle)
                    val alertTvContent = view.findViewById<TextView>(R.id.alertTvContent)
                    alertTvContent.visibility = View.GONE
                    val alertBtnOk = view.findViewById<Button>(R.id.alertBtnOk)

                    alertTvTitle.text = "메모삭제"

                    alertBtnOk.setOnClickListener {
                        RetrofitClient.api.deleteTodoMemo(todoCmplList[position].cmpl_seq!!).enqueue(object :
                            Callback<ResponseBody> {
                            override fun onResponse(
                                call: Call<ResponseBody>,
                                response: Response<ResponseBody>
                            ) {
                                val res = response.body()?.string()
                                if (res.toString() == "1") {
                                    Toast.makeText(
                                        context,
                                        "메모를 삭제했습니다.",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    alertDialog.dismiss()
                                }else{
                                    Toast.makeText(context, "메모 삭제 실패", Toast.LENGTH_SHORT).show()
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
            }else{
                holder.todoCplRvMemoImg.visibility = View.GONE
                holder.todoCplRvMemoDelete.visibility = View.GONE
            }
            holder.todoCplRvMemo.text = todoCmplList[position].cmpl_memo
        }

        if (todoCmplList[position].cmpl_img == "") {
            holder.todoCplRvClBg.setBackgroundColor(
                Color.parseColor(
                    "#000000"
                )
            )
            holder.todoCplRvClPic.visibility = View.GONE
        }
    }

    override fun getItemCount(): Int {
        return todoCmplList.size
    }
}