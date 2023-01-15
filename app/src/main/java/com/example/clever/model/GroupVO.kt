package com.example.clever.model

import com.google.gson.annotations.SerializedName

data class GroupVO(
    @SerializedName("group_seq")
    val group_seq: Int,

    @SerializedName("group_name")
    val group_name: String?,

    @SerializedName("group_dt")
    val group_dt: String?,

    @SerializedName("group_serial")
    val group_serial: String?,

    @SerializedName("mem_id")
    val mem_id: String?,

    @SerializedName("join_dt")
    val join_dt: String?,

    @SerializedName("mem_name")
    val mem_name: String?
) {
    constructor(group_seq: Int) : this(group_seq, null, null, null, null, null, null)
    constructor(group_seq: Int, mem_id: String) : this(group_seq, null, null, null, mem_id, null, null)
}
