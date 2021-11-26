package com.xemic.lazybird.models.retrofit

import com.google.gson.annotations.SerializedName

data class Jwt(
    val code: Int,
    val msg: String,
    val token: String
){
    override fun toString(): String {
        return "code:${code}, msg:${msg}, token:${token}"
    }
}
