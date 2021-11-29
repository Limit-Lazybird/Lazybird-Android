package com.xemic.lazybird.models.retrofit

data class LoginResponseBody(
    val jwt: Jwt,
    val refreshToken: String,
    val useYN: String,
    val code: Int,
    val msg: String
)