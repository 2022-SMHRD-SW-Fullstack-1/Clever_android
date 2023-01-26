package com.example.clever.view.profile

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.clever.R
import com.example.clever.databinding.ActivityProfileChangeBinding
import com.example.clever.model.Member
import com.example.clever.retrofit.RetrofitClient
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileChangeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileChangeBinding

    lateinit var loginSp: SharedPreferences
    private lateinit var memId: String
    private lateinit var memName:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_change)

        loginSp = getSharedPreferences("loginInfo", Context.MODE_PRIVATE)
        memId = loginSp.getString("mem_id", "").toString()
        memName = loginSp.getString("mem_name","").toString()

        binding = ActivityProfileChangeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.profileChangeEtName.setText(memName)
        binding.profileChangeEtPhone.setText(memId)
        binding.profileChangeEtPhone.isEnabled = false

        binding.profileChangeImgBack.setOnClickListener {
            finish()
        }

        binding.profileChangeBtnProfile.setOnClickListener {
            chName()
        }
    }

    private fun chName(){
        val name = binding.profileChangeEtName.text.toString()
        val req = Member(memId, null, name, null)
        RetrofitClient.api.chName(req).enqueue(object : Callback<ResponseBody>{
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                val res = response.body()?.string()
                if(res.toString() == "1"){
                    Toast.makeText(this@ProfileChangeActivity, "이름이 변경 되었습니다.", Toast.LENGTH_SHORT).show()
                    val editorMem = loginSp.edit()
                    editorMem.putString("mem_name", name).commit()
                    finish()
                }else{
                    Toast.makeText(this@ProfileChangeActivity, "이름 변경에 실패하였습니다.", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })
    }
}