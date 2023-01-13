package com.example.clever.model

import com.google.gson.annotations.SerializedName

data class Member(
    @SerializedName("mem_id")
    var mem_id: String,

    @SerializedName("mem_pw")
    var mem_pw: String,

    @SerializedName("mem_name")
    var mem_name: String?,

    @SerializedName("mem_email")
    var mem_email: String?,

    @SerializedName("mem_joindate")
    var mem_joinDate: String?
) {
    constructor(mem_id: String, mem_pw: String) : this(mem_id, mem_pw, null, null, null)
}