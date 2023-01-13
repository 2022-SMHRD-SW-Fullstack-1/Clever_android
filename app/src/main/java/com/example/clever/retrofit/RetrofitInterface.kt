package com.example.clever.retrofit

import com.example.clever.model.Member
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface RetrofitInterface {

    @GET("android/hello")
    fun hello(): Call<ResponseBody>

    @POST("android/join")
    fun join(
        @Body join_info: Member
    ): Call<ResponseBody>

    @POST("android/login")
    fun login(
        @Body login_info: Member
    ): Call<ResponseBody>

    @GET("getGroup")
    fun getGroup(

    ): Call<ResponseBody>

}