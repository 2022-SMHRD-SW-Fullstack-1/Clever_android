package com.example.clever.view.home.cal

import android.app.TimePickerDialog
import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.example.clever.R
import com.example.clever.databinding.ActivityAttChangeBinding
import com.example.clever.model.AttendanceVO
import com.example.clever.model.ChangeAttendanceVO
import com.example.clever.retrofit.RetrofitClient
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import java.util.logging.SimpleFormatter

class AttChangeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAttChangeBinding

    lateinit var att_seq: String

    lateinit var start_time: String
    lateinit var end_time: String

    lateinit var start_ch: String
    lateinit var end_ch: String

    lateinit var memId: String
    lateinit var att_date: String
    lateinit var group_seq: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_att_change)

        binding = ActivityAttChangeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        att_seq = intent.getStringExtra("att_seq").toString()
        getAtt()

        binding.calChangeImgBack.setOnClickListener {
            finish()
        }

        binding.calChangeBtnReq.setOnClickListener {
            attCh()
        }

        binding.AttChBtn.setOnClickListener {
            val timeSetListener = TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                if (hourOfDay < 10) {
                    start_ch = "0${hourOfDay}:${minute}"
                    if (minute < 10) {
                        start_ch = "0${hourOfDay}:0${minute}"
                    }
                } else {
                    start_ch = "${hourOfDay}:${minute}"
                    if (minute < 10) {
                        start_ch = "${hourOfDay}:0${minute}"
                    }
                }
                //???????????? ????????? ?????? ????????? ?????? ??????
                binding.attStartCh.text = start_ch

                val timeSetListener =
                    TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                        if (hourOfDay < 10) {
                            end_ch = "0${hourOfDay}:${minute}"
                            if (minute < 10) {
                                end_ch = "0${hourOfDay}:0${minute}"
                            }
                        } else {
                            end_ch = "${hourOfDay}:${minute}"
                            if (minute < 10) {
                                end_ch = "${hourOfDay}:0${minute}"
                            }
                        }
                        //???????????? ????????? ?????? ????????? ?????? ??????
                        binding.attEndCh.text = end_ch
                    }

                TimePickerDialog(
                    this,
                    timeSetListener,
                    end_time.substring(0, 2).toInt(),
                    end_time.substring(3, 5).toInt(),
                    false
                ).show()
            }

            TimePickerDialog(
                this,
                timeSetListener,
                start_time.substring(0, 2).toInt(),
                start_time.substring(3, 5).toInt(),
                false
            ).show()
        }

    }

    private fun getAtt() {
        val req = AttendanceVO(att_seq.toInt())
        RetrofitClient.api.getAtt(req).enqueue(object : Callback<AttendanceVO> {
            override fun onResponse(call: Call<AttendanceVO>, response: Response<AttendanceVO>) {
                val res = response.body()!!
                Log.d("getAtt", res.toString())
                start_time = res.att_sche_start_time.toString().substring(0, 5)
                end_time = res.att_sche_end_time.toString().substring(0, 5)
                binding.attStart.text = start_time
                binding.attEnd.text = end_time
                binding.attStartCh.text = start_time
                binding.attEndCh.text = end_time

                att_date = res.att_date.toString()
                binding.attChTvDate.text = att_date

                memId = res.mem_id.toString()
                group_seq = res.group_seq.toString()

                val formatter = SimpleDateFormat("yyyy-MM-dd")
                val dateTime = formatter.parse(att_date)
                val cal = Calendar.getInstance()
                cal.time = dateTime
                val dayNum = cal.get(Calendar.DAY_OF_WEEK)
                when (dayNum) {
                    1 -> binding.attChTvDay.text = "?????????"
                    2 -> binding.attChTvDay.text = "?????????"
                    3 -> binding.attChTvDay.text = "?????????"
                    4 -> binding.attChTvDay.text = "?????????"
                    5 -> binding.attChTvDay.text = "?????????"
                    6 -> binding.attChTvDay.text = "?????????"
                    7 -> binding.attChTvDay.text = "?????????"
                }
            }

            override fun onFailure(call: Call<AttendanceVO>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })
    }

    private fun attCh() {
        val req = ChangeAttendanceVO(
            att_seq.toInt(),
            memId,
            start_ch,
            end_ch,
            att_date,
            group_seq.toInt()
        )
        Log.d("req", req.toString())
        RetrofitClient.api.attCh(req).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                val res = response.body()?.string()
                Log.d("????????????", res.toString())
                if(res.toString() == "1"){
                    Toast.makeText(this@AttChangeActivity, "????????? ?????????????????????.", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })

    }
}