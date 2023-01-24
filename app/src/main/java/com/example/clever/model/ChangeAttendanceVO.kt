package com.example.clever.model

import com.google.gson.annotations.SerializedName

data class ChangeAttendanceVO(
    @SerializedName("ch_seq")
    val ch_seq: Int?,

    @SerializedName("att_seq")
    val att_seq: Int?,

    @SerializedName("ch_approve")
    val ch_approve: String?,

    @SerializedName("ch_reject_memo")
    val ch_reject_memo: String?,

    @SerializedName("mem_id")
    val mem_id: String?,

    @SerializedName("ch_start_time")
    val ch_start_time: String?,

    @SerializedName("ch_end_time")
    val ch_end_time: String?,

    @SerializedName("ch_date")
    val ch_date: String?,

    @SerializedName("group_seq")
    val group_seq: Int?,
) {
    constructor(att_seq: Int):this(null, att_seq, null, null, null, null, null, null, null)
    constructor(
        att_seq: Int,
        mem_id: String?,
        ch_start_time: String?,
        ch_end_time: String?,
        ch_date: String?,
        group_seq: Int?
    ) : this(null, att_seq, null, null, mem_id, ch_start_time, ch_end_time, ch_date, group_seq)
}