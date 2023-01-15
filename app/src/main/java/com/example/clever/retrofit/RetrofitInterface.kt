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

    @POST("android/userInfo")
    fun userInfo(
        @Body user_info: Member
    ): Call<Member>

    @POST("android/getGroup")
    fun getGroup(
        @Body user_info: Member
    ): Call<List<GroupVO>>

    @POST("android/groupInfo")
    fun groupInfo(
        @Body group_info: GroupVO
    ): Call<GroupVO>

    @POST("android/groupMemCount")
    fun groupMemCount(
        @Body group_info : GroupVO
    ): Call<List<GroupVO>>

    @POST("android/joinDate")
    fun joinDate(
        @Body group_info: GroupVO
    ): Call<GroupVO>

    @POST("android/groupMem")
    fun groupMem(
        @Body group_info : GroupVO
    ): Call<List<GroupVO>>
}