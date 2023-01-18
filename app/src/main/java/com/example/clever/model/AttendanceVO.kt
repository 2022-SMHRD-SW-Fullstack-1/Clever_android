package com.example.clever.model

import com.google.gson.annotations.SerializedName

class AttendanceVO(
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

    @SerializedName("select_date")
    val select_date: String?,
) {
    constructor(mem_id: String) : this(null, mem_id, null, null, null, null, null, null, null)
    constructor(mem_id: String, select_date: String) : this(
        null,
        mem_id,
        null,
        null,
        null,
        null,
        null,
        null,
        select_date
    )
}