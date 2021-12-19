package com.limit.lazybird.models.retrofit

data class Jwt(
    val code: Int,
    val msg: String,
    val token: String
){
    override fun toString(): String {
        return "code:${code}, msg:${msg}, token:${token}"
    }
}
