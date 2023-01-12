package com.example.clever.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.clever.R
import com.example.clever.databinding.ActivityHelloBinding
import com.example.clever.retrofit.RetrofitClient
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class HelloActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHelloBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hello)

        binding = ActivityHelloBinding.inflate(layoutInflater)
        setContentView(binding.root)

        hello()
    }

    fun hello(){
        RetrofitClient.api.hello().enqueue(object : Callback<ResponseBody>{
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                binding.helloTv.text = response.body()?.string()
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.d("hello", t.localizedMessage)
            }
        })
    }

}