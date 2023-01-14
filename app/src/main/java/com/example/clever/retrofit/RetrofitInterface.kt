package com.example.clever.retrofit

import com.example.clever.model.GroupVO
import com.example.clever.model.Member
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface RetrofitInterface {

    @GET("android/hello")
    fun hello(): Call<ResponseBody>

    @POST("join")
    fun join(
        @Body join_info: Member
    ): Call<ResponseBody>

    @POST("login")
    fun login(
        @Body login_info: Member
    ): Call<ResponseBody>

    @POST("android/getGroup")
    fun getGroup(
        @Body user_info: Member
    ): Call<List<GroupVO>>

}