package com.example.clever.model

import com.google.gson.annotations.SerializedName

data class Member(
    @SerializedName("mem_seq")
    var memSeq: Int,

    @SerializedName("mem_name")
    var memName: String,

    @SerializedName("mem_phone")
    var memPhone: String,

    @SerializedName("mem_pw")
    var memPw: String,

    @SerializedName("mem_date")
    var memDate: String
)