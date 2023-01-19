package com.example.clever.model

import com.google.gson.annotations.SerializedName

data class AttendanceVO(
    @SerializedName("att_seq")
    val att_seq: Int?,

    @SerializedName("mem_id")
    val mem_id: String?,

    @SerializedName("att_date")
    val att_date: String?,

    @SerializedName("att_sche_start_time")
    val att_sche_start_time: String?,

    @SerializedName("att_sche_end_time")
    val att_sche_end_time: String?,

    @SerializedName("att_real_start_time")
    val att_real_start_time: String?,

    @SerializedName("att_real_end_time")
    val att_real_end_time: String?,

    @SerializedName("group_seq")
    val group_seq: Int?,

    @SerializedName("mem_name")
    val mem_name: String?,

    @SerializedName("select_date")
    val select_date: String?,
) {
    constructor(mem_id: String, group_seq:Int) : this(null, mem_id, null, null, null, null, null, group_seq, null, null)
    constructor(mem_id: String, select_date: String, group_seq:Int) : this(
        null,
        mem_id,
        null,
        null,
        null,
        null,
        null,
        group_seq,
        null,
        select_date
    )
}