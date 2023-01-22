package com.example.clever.view.splash

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import com.example.clever.R
import com.example.clever.model.GroupVO
import com.example.clever.model.Member
import com.example.clever.retrofit.RetrofitClient
import com.example.clever.view.LoginActivity
import com.example.clever.view.MainActivity
import com.example.clever.view.home.HomeActivity
import com.google.gson.Gson
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.properties.Delegates

class MainSplashActivity : AppCompatActivity() {

    lateinit var loginSp: SharedPreferences
    private lateinit var memId: String
    private lateinit var memName:String
    lateinit var autoSp: SharedPreferences
    private lateinit var autoLoginPhone: String
    private lateinit var autoLoginPw: String
    private var autoLoginCheckBox by Delegates.notNull<Boolean>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_splash)

        loginSp = getSharedPreferences("loginInfo", Context.MODE_PRIVATE)
        memId = loginSp.getString("mem_id", "").toString()
        memName = loginSp.getString("mem_name","").toString()

        autoSp = getSharedPreferences("autoLoginInfo", Context.MODE_PRIVATE)
        autoLoginPhone = autoSp.getString("loginPhone", "").toString()
        autoLoginPw = autoSp.getString("loginPw", "").toString()
        autoLoginCheckBox = autoSp.getBoolean("loginCb", false)

        Handler().postDelayed({
            if (!autoLoginCheckBox) {
                val intent = Intent(this@MainSplashActivity, LoginActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                autoLogin()
            }

        }, 1000)
    }

    private fun autoLogin(){
        val loginInfo = Member(autoLoginPhone, autoLoginPw)
        RetrofitClient.api.login(loginInfo).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(
                call: Call<ResponseBody>,
                response: Response<ResponseBody>
            ) {
                val res = response.body()?.string()
                if (res != "") {
                    val editorMem = loginSp.edit()
                    val memberInfo = Gson().fromJson(res, Member::class.java)
                    editorMem.putString("mem_id", memberInfo.mem_id)
                    editorMem.putString("mem_name", memberInfo.mem_name)
                    editorMem.apply()

                    getGroup()
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                t.localizedMessage?.let { Log.d("login", it) }
            }
        })
    }

    // 참여중이 group 가져오기
    private fun getGroup() {
        RetrofitClient.api.getGroup(Member(memId)).enqueue(object : Callback<List<GroupVO>> {
            override fun onResponse(call: Call<List<GroupVO>>, response: Response<List<GroupVO>>) {
                val res = response.body()
                if (res?.size == 1) {
                    val intent = Intent(this@MainSplashActivity, HomeActivity::class.java)
                    intent.putExtra("group_seq", "${res[0].group_seq}")
                    intent.putExtra("group_name", res[0].group_name)
                    startActivity(intent)
                    finish()
                } else {
                    val intent = Intent(this@MainSplashActivity, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }

            override fun onFailure(call: Call<List<GroupVO>>, t: Throwable) {
                t.localizedMessage?.let { Log.d("group", it) }
            }
        })
    }
}