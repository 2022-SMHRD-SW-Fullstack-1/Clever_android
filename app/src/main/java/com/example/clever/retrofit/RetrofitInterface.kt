package com.example.clever.retrofit

import com.example.clever.model.Member
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface RetrofitInterface {

    @GET("android/hello")
    fun hello(): Call<ResponseBody>

    @FormUrlEncoded
    @POST("join")
    fun joinMember(
        @Field("name") name: String,
        @Field("phone") phone: String,
        @Field("password") password: String
    ): Call<Member>

}