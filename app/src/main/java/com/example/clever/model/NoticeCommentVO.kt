package com.example.clever.model

import com.google.gson.annotations.SerializedName

data class NoticeCommentVO(
    @SerializedName("com_seq")
    val com_seq: Int?,

    @SerializedName("notice_seq")
    val notice_seq: Int?,

    @SerializedName("mem_id")
    val mem_id: String?,

    @SerializedName("mem_name")
    val mem_name: String?,

    @SerializedName("com_content")
    val com_content: String?,

    @SerializedName("com_time")
    val com_time: String?,
){
    constructor(notice_seq: Int?):this(null, notice_seq, null, null, null, null)
}