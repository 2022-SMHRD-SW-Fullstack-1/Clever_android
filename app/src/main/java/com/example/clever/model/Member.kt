package com.example.clever.model

import com.google.gson.annotations.SerializedName

data class Member(
    @SerializedName("mem_id")
    var mem_id: String,

    @SerializedName("mem_pw")
    var mem_pw: String?,

    @SerializedName("mem_name")
    var mem_name: String?,

    @SerializedName("mem_email")
    var mem_email: String?
) {
    constructor(mem_id: String) : this(mem_id, null, null, null )
    constructor(mem_id: String, mem_pw: String) : this(mem_id, mem_pw, null, null )
    constructor(mem_id: String, mem_pw: String?, mem_email: String) : this(mem_id, mem_pw, null, mem_email )
}