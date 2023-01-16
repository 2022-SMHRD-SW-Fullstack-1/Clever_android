package com.example.clever.model

import com.google.gson.annotations.SerializedName

data class CategoryVO(
    @SerializedName("cate_seq")
    val cate_seq: Int?,

    @SerializedName("cate_type")
    val cate_type: String?,

    @SerializedName("cate_name")
    val cate_name: String?,

    @SerializedName("group_seq")
    val group_seq: Int,

    @SerializedName("count")
    val count: Int?,
) {
    constructor(group_seq: Int, cate_type: String) : this(null, cate_type, null, group_seq, null)
}