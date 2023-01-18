package com.example.clever.model

import com.google.gson.annotations.SerializedName

data class ToDoVo(
    @SerializedName("todo_seq")
    val todo_seq: Int?,

    @SerializedName("cate_seq")
    val cate_seq: Int?,

    @SerializedName("todo_title")
    val todo_title: String?,

    @SerializedName("todo_content")
    val todo_content: String?,

    @SerializedName("todo_dt")
    val todo_dt: String?,

    @SerializedName("todo_repeat")
    val todo_repeat: String?,

    @SerializedName("todo_method")
    val todo_method: String?,

    @SerializedName("mem_id")
    val mem_id: String?,

    @SerializedName("mem_name")
    val mem_name: String?,

    @SerializedName("cate_name")
    val cate_name: String?,

    @SerializedName("select_day")
    var select_day: String?,
){
    constructor(cate_seq: Int): this(null, cate_seq, null, null, null, null, null, null, null, null, null)
    constructor(todo_seq: Int?, cate_seq: Int): this(todo_seq, cate_seq, null, null, null, null, null, null, null, null, null)
}