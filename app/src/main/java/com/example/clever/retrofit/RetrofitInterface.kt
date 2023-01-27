package com.example.clever.retrofit

import com.example.clever.model.*
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
        @Body group_info: GroupVO
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

    @POST("android/getToDoList")
    fun getToDoList(
        @Body todo_info: ToDoVo
    ): Call<List<ToDoVo>>

    @FormUrlEncoded
    @POST("android/getMyToDo")
    fun getMyToDo(
        @Field("group_seq") group_seq: Int,
        @Field("mem_id") mem_id: String,
    ): Call<List<ToDoVo>>

    @POST("android/getToDoComplete")
    fun getToDoComplete(
        @Body todo_info: ToDoCompleteVO
    ): Call<List<ToDoCompleteVO>>

    @FormUrlEncoded
    @POST("android/getMyToDoComplete")
    fun getMyToDoComplete(
        @Field("group_seq") group_seq:Int,
        @Field("mem_id") mem_id: String,
        @Field("selectDate") selectDate: String,
    ): Call<List<ToDoCompleteVO>>

    @POST("android/getToDo")
    fun getToDo(
        @Body todo_info: ToDoVo
    ): Call<ToDoVo>

    @FormUrlEncoded
    @POST("android/getToDoCmplList")
    fun getToDoCmplList(
        @Field("todo_seq") todo_seq: Int,
    ): Call<List<ToDoCompleteVO>>

    @POST("android/getCode")
    fun getCode(
        @Body mem_info: Member
    ): Call<ResponseBody>

    @POST("android/changePw")
    fun changePw(
        @Body mem_info: Member
    ): Call<ResponseBody>

    @POST("android/groupOut")
    fun groupOut(
        @Body group_info: GroupVO
    ): Call<ResponseBody>

    @POST("android/categoryDelete")
    fun categoryDelete(
        @Body cate_info: CategoryVO
    ): Call<ResponseBody>

    @POST("android/noticeDelete")
    fun noticeDelete(
        @Body notice_info: NoticeVO
    ): Call<ResponseBody>

    @POST("android/todoCmpl")
    fun todoCmpl(
        @Body todo_info: ToDoCompleteVO
    ): Call<ResponseBody>

    @POST("android/getAttendance")
    fun getAttendance(
        @Body att_info: AttendanceVO
    ): Call<List<AttendanceVO>>

    @POST("android/getAtt")
    fun getAtt(
        @Body att_info: AttendanceVO
    ): Call<AttendanceVO>

    @POST("android/attCh")
    fun attCh(
        @Body att_info: ChangeAttendanceVO
    ): Call<ResponseBody>

    @POST("android/chName")
    fun chName(
        @Body mem_info: Member
    ): Call<ResponseBody>

    @POST("android/withdrawal")
    fun withdrawal(
        @Body mem_info: Member
    ): Call<ResponseBody>

    @POST("android/noticeWrite")
    fun noticeWrite(
        @Body notice_info: NoticeVO
    ): Call<ResponseBody>

    @POST("android/joinGroup")
    fun joinGroup(
        @Body group_info: GroupVO
    ): Call<ResponseBody>

    @POST("android/writeComment")
    fun writeComment(
        @Body com_info: NoticeCommentVO
    ): Call<ResponseBody>

    @POST("android/getComment")
    fun getComment(
        @Body com_info: NoticeCommentVO
    ): Call<List<NoticeCommentVO>>

    @POST("android/deleteComment")
    fun deleteComment(
        @Body com_info: NoticeCommentVO
    ): Call<ResponseBody>

    @POST("android/checkAttCh")
    fun checkAttCh(
        @Body att_info: ChangeAttendanceVO
    ): Call<ResponseBody>

    @FormUrlEncoded
    @POST("android/getAttTime")
    fun getAttTime(
        @Field("mem_id") mem_id: String,
        @Field("group_seq") group_seq: Int,
        @Field("start_date") start_date: String,
        @Field("end_date") end_date: String,
    ): Call<ResponseBody>

    @FormUrlEncoded
    @POST("android/getMemo")
    fun getMemo(
        @Field("cmpl_time") cmpl_time: String,
        @Field("group_seq") group_seq: Int,
    ): Call<List<ToDoCompleteVO>>

    @FormUrlEncoded
    @POST("android/deleteTodoMemo")
    fun deleteTodoMemo(
        @Field("cmpl_seq") cmpl_seq: Int,
    ): Call<ResponseBody>

    @FormUrlEncoded
    @POST("android/getCmpl")
    fun getCmpl(
        @Field("cmpl_seq") cmpl_seq: Int,
    ): Call<ToDoCompleteVO>

    @POST("android/todoModify")
    fun todoModify(
        @Body cmpl_info: ToDoCompleteVO
    ): Call<ResponseBody>
}