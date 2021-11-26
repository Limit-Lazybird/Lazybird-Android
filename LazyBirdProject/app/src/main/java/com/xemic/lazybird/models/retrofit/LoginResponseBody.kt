package com.xemic.lazybird.models.retrofit

data class LoginResponseBody(
    val jwt: Jwt,
    val useYN: String
)