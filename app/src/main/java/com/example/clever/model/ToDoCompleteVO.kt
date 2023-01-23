package com.example.clever.model

import com.google.gson.annotations.SerializedName

data class ToDoCompleteVO(
    @SerializedName("cmpl_seq")
    val cmpl_seq: Int?,

    @SerializedName("todo_seq")
    val todo_seq: Int?,

    @SerializedName("mem_id")
    val mem_id: String?,

    @SerializedName("cmpl_time")
    val cmpl_time: String?,

    @SerializedName("cmpl_img")
    val cmpl_img: String?,

    @SerializedName("cmpl_memo")
    val cmpl_memo: String?,

    @SerializedName("cmpl_strange")
    val cmpl_strange: String?,

    @SerializedName("cate_seq")
    val cate_seq: Int?,

    @SerializedName("mem_name")
    val mem_name: String?,

    @SerializedName("todo_title")
    val todo_title: String?,
) {
    constructor(cate_seq: Int) : this(null, null, null, null, null, null, null, cate_seq, null, null)
    constructor(cate_seq: Int, cmpl_time: String?) : this(null, null, null, cmpl_time, null, null, null, cate_seq, null, null)
    constructor(todo_seq: Int?, cate_seq: Int, todo_title: String?) : this(null, todo_seq, null, null, null, null, null, cate_seq, null, todo_title)
    constructor(todo_seq: Int?, mem_id: String?, cmpl_img:String?, cmpl_strange:String, cate_seq: Int) : this(null, todo_seq, mem_id, null, cmpl_img, null, cmpl_strange, cate_seq, null, null)
    constructor(todo_seq: Int?, mem_id: String?, cmpl_img:String?, cmpl_memo: String?, cmpl_strange:String, cate_seq: Int) : this(null, todo_seq, mem_id, null, cmpl_img, cmpl_memo, cmpl_strange, cate_seq, null, null)
}