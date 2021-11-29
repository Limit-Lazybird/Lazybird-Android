package com.xemic.lazybird.models

data class LoginInfo(
    val loginType: String,
    val email: String,
    val name: String,
    val token: String
)
