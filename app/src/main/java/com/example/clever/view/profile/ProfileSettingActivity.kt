package com.example.clever.view.profile

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.*
import com.example.clever.R
import com.example.clever.databinding.ActivityProfileSettingBinding
import com.example.clever.model.Member
import com.example.clever.retrofit.RetrofitClient
import com.example.clever.view.LoginActivity
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileSettingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileSettingBinding

    lateinit var loginSp: SharedPreferences
    private lateinit var memId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_setting)

        loginSp = getSharedPreferences("loginInfo", Context.MODE_PRIVATE)
        memId = loginSp.getString("mem_id", "").toString()

        binding = ActivityProfileSettingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.profileSettingImgBack.setOnClickListener {
            finish()
        }

        binding.profileSettingllAlert.setOnClickListener {
            val intent = Intent(this@ProfileSettingActivity, ProfileAlertActivity::class.java)
            startActivity(intent)
        }

        binding.profileSettingllPw.setOnClickListener {
            val intent = Intent(this@ProfileSettingActivity, ProfilePwActivity::class.java)
            startActivity(intent)
        }

        binding.profileSettingBtnWithdraw.setOnClickListener {
            withdrawal()
        }
    }

    private fun withdrawal() {
        val layoutInflater = LayoutInflater.from(this@ProfileSettingActivity)
        val view = layoutInflater.inflate(R.layout.custom_dialog_alert, null)

        val alertDialog =
            AlertDialog.Builder(this@ProfileSettingActivity, R.style.CustomAlertDialog)
                .setView(view).create()

        val alertBtnCancel = view.findViewById<ImageButton>(R.id.alertBtnCancel)
        val alertTvTitle = view.findViewById<TextView>(R.id.alertTvTitle)
        val alertTvContent = view.findViewById<TextView>(R.id.alertTvContent)
        val alertBtnOk = view.findViewById<Button>(R.id.alertBtnOk)

        alertTvTitle.text = "회원탈퇴"
        alertTvContent.text = "정말로 회원탈퇴 하시겠습니까 ?"

        alertBtnOk.setOnClickListener {
            alertDialog.dismiss()

            val viewInput = layoutInflater.inflate(R.layout.custom_dialog_alert_input, null)
            val inputDialog = AlertDialog.Builder(this@ProfileSettingActivity, R.style.CustomAlertDialog).setView(viewInput).create()

            val alertInputTvTitle = viewInput.findViewById<TextView>(R.id.alertInputTvTitle)
            val alertInputEt = viewInput.findViewById<EditText>(R.id.alertInputEt)
            val alertInputBtnCancel = viewInput.findViewById<Button>(R.id.alertInputBtnCancel)
            val alertInputBtnAccept = viewInput.findViewById<TextView>(R.id.alertInputBtnAccept)

            alertInputTvTitle.text = "비밀번호입력"

            alertInputBtnAccept.setOnClickListener {
                val pw = alertInputEt.text.toString()
                val req = Member(memId, pw)
                RetrofitClient.api.withdrawal(req).enqueue(object : Callback<ResponseBody>{
                    override fun onResponse(
                        call: Call<ResponseBody>,
                        response: Response<ResponseBody>
                    ) {
                        val res = response.body()?.string()
                        if(res.toString() == "1"){
                            Toast.makeText(this@ProfileSettingActivity, "회원탈퇴 되었습니다.", Toast.LENGTH_SHORT).show()
                            val intent = Intent(this@ProfileSettingActivity, LoginActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            startActivity(intent)
                        }else{
                            Toast.makeText(this@ProfileSettingActivity, "비밀번호를 확인해주세요", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                        TODO("Not yet implemented")
                    }
                })
            }


            alertInputBtnCancel.setOnClickListener {
                inputDialog.dismiss()
            }
            inputDialog.show()

        }
        alertBtnCancel.setOnClickListener {
            alertDialog.dismiss()
        }
        alertDialog.show()
    }
}