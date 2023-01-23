package com.example.clever.model

import com.google.gson.annotations.SerializedName

data class NoticeVO(
    @SerializedName("notice_seq")
    val notice_seq: Int?,

    @SerializedName("cate_seq")
    val cate_seq: Int?,

    @SerializedName("notice_title")
    val notice_title: String?,

    @SerializedName("notice_content")
    val notice_content: String?,

    @SerializedName("notice_dt")
    val notice_dt: String?,

    @SerializedName("notice_photo")
    val notice_photo: String?,

    @SerializedName("mem_id")
    val mem_id: String?,

    @SerializedName("mem_name")
    val mem_name: String?,
) {
    constructor(cate_seq: Int) : this(null, cate_seq, null, null, null, null, null, null)
    constructor(notice_seq: Int, cate_seq: Int) : this(
        notice_seq,
        cate_seq,
        null,
        null,
        null,
        null,
        null,
        null
    )

    constructor(
        cate_seq: Int,
        notice_title: String?,
        notice_content: String?,
        notice_photo: String?,
        mem_id: String?
    ) : this(null, cate_seq, notice_title, notice_content, null, notice_photo, mem_id, null)
}