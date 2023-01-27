package com.example.clever.view.home.cal

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.example.clever.R
import com.example.clever.databinding.ActivityCalculateBinding
import com.example.clever.retrofit.RetrofitClient
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.DecimalFormat
import kotlin.math.round

class CalculateActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCalculateBinding

    lateinit var mem_id: String
    lateinit var group_seq: String
    var attTime: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calculate)

        mem_id = intent.getStringExtra("mem_id").toString()
        group_seq = intent.getStringExtra("group_seq").toString()

        binding = ActivityCalculateBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val selectedDate = intent.getStringExtra("selectedDate").toString()
        binding.calEtStartYear.setText(selectedDate.substring(0, 4))
        binding.calEtStartMonth.setText(selectedDate.substring(5, 7))
        binding.calEtStartDay.setText("01")
        binding.calEtEndYear.setText(selectedDate.substring(0, 4))
        binding.calEtEndMonth.setText(selectedDate.substring(5, 7))
        binding.calEtEndDay.setText(when(selectedDate.substring(5,7)){
            "01", "03", "05", "07", "08", "10", "12" -> "31"
            "04", "06", "09", "11" -> "30"
            else -> "29"
        })

        binding.calculateCl.setOnTouchListener { _, _ ->
            hideKeyboard()
            false
        }

        binding.calEtStartYear.requestFocus()
        binding.calEtEndDay.setOnKeyListener { view, i, event ->
            if (event.action == KeyEvent.ACTION_DOWN && i == KeyEvent.KEYCODE_ENTER) {
                hideKeyboard()
                true
            } else {
                false
            }
        }

        binding.calCalculateImgBack.setOnClickListener {
            finish()
        }

        binding.calBtnGetTime.setOnClickListener {
            hideKeyboard()
            getAttTime()
        }

        binding.calEtHourly.setOnKeyListener { view, i, event ->
            if (event.action == KeyEvent.ACTION_DOWN && i == KeyEvent.KEYCODE_ENTER) {
                hideKeyboard()
                true
            } else {
                false
            }
        }
        binding.calEtHourly.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                val dec = DecimalFormat("#,##0")
                if (binding.calEtHourly.text.toString() != "") {
                    val hourly = binding.calEtHourly.text.toString().toInt()
                    var salary = attTime * hourly / 10
                    binding.calTvSalary.text = "${dec.format(salary.toInt() * 10)}원"
                } else {
                    binding.calTvSalary.text = "0원"
                }
            }
        })
    }


    private fun hideKeyboard() {
        if (this.currentFocus != null) {
            val inputManager: InputMethodManager =
                this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputManager.hideSoftInputFromWindow(
                this.currentFocus!!.windowToken,
                InputMethodManager.HIDE_NOT_ALWAYS
            )
        }
    }

    private fun getAttTime() {
        var startY = binding.calEtStartYear.text.toString()
        var startM = binding.calEtStartMonth.text.toString()
        var startD = binding.calEtStartDay.text.toString()
        var endY = binding.calEtEndYear.text.toString()
        var endM = binding.calEtEndMonth.text.toString()
        var endD = binding.calEtEndDay.text.toString()

        if (startM.toInt() < 10) startM = "0${startM.toInt()}"
        if (endM.toInt() < 10) endM = "0${endM.toInt()}"
        if (startD.toInt() < 10) startD = "0${startD.toInt()}"
        if (endD.toInt() < 10) endD = "0${endD.toInt()}"

        val start_date = "${startY}-${startM}-${startD}"
        val end_date = "${endY}-${endM}-${endD}"

        if (startY.toInt() > endY.toInt() || startY.length != 4 || endY.length != 4 || startY.toInt() == 0 || startM.toInt() == 0 || startD.toInt() == 0 || endY.toInt() == 0 || endM.toInt() == 0 || endD.toInt() == 0 || startM.toInt() > 12 || endM.toInt() > 12 || startD.toInt() > 31 || endD.toInt() > 31) {
            Toast.makeText(this@CalculateActivity, "날짜를 정확하게 입력해주세요", Toast.LENGTH_SHORT).show()
        } else {
            RetrofitClient.api.getAttTime(mem_id, group_seq.toInt(), start_date, end_date)
                .enqueue(object : Callback<ResponseBody> {
                    override fun onResponse(
                        call: Call<ResponseBody>,
                        response: Response<ResponseBody>
                    ) {
                        val res = response.body()?.string()
                        attTime = round(res.toString().toDouble() * 100) / 100
                        binding.calEtAttTime.setText(attTime.toString())
                        val dec = DecimalFormat("#,##0")
                        if (binding.calEtHourly.text.toString() != "") {
                            val hourly = binding.calEtHourly.text.toString().toInt()
                            var salary = attTime * hourly / 10
                            binding.calTvSalary.text = "${dec.format(salary.toInt() * 10)}원"
                        } else {
                            binding.calTvSalary.text = "0원"
                        }
                    }

                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                        TODO("Not yet implemented")
                    }
                })
        }
    }
}