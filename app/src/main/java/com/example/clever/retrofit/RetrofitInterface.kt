package com.example.clever.retrofit

import com.example.clever.model.CategoryVO
import com.example.clever.model.GroupVO
import com.example.clever.model.Member
import com.example.clever.model.NoticeVO
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface RetrofitInterface {

    @GET("android/hello")
    fun hello(): Call<ResponseBody>

    // 회원가입
    @POST("android/joinMember")
    fun joinMember(
        @Body join_info: Member
    ): Call<ResponseBody>

    // 로그인
    @POST("login")
    fun login(
        @Body login_info: Member
    ): Call<ResponseBody>
    
    // mainActivity groupList 가져오기
    @POST("android/getGroup")
    fun getGroup(
        @Body user_info: Member
    ): Call<List<GroupVO>>

    @POST("android/groupInfo")
    fun groupInfo(
        @Body group_info: GroupVO
    ): Call<GroupVO>

    @POST("android/joinDate")
    fun joinDate(
        @Body group_info: GroupVO
    ): Call<GroupVO>

    @POST("android/userInfo")
    fun userInfo(
        @Body user_info: Member
    ): Call<Member>

    @POST("android/groupMem")
    fun groupMem(
        @Body group_info : GroupVO
    ): Call<List<GroupVO>>

    @POST("android/getCategory")
    fun getCategory(
        @Body cate_info: CategoryVO
    ): Call<List<CategoryVO>>

    @POST("android/getNotice")
    fun getNotice(
        @Body cate_info: NoticeVO
    ): Call<List<NoticeVO>>

    @POST("android/getNoticeDetail")
    fun getNoticeDetail(
        @Body notice_info: NoticeVO
    ): Call<NoticeVO>
}