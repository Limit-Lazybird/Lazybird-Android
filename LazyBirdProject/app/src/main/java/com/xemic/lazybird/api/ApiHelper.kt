package com.xemic.lazybird.api

import com.xemic.lazybird.models.Todo
import com.xemic.lazybird.models.retrofit.*
import retrofit2.Response

interface ApiHelper {
    suspend fun getTodos(): Response<List<Todo>>

    suspend fun loginKakao(
        email: String,
        token: String,
        name: String
    ): Response<LoginResponseBody>
    suspend fun loginGoogle(
        email: String,
        token: String,
        name: String
    ): Response<LoginResponseBody>

    suspend fun getEarlyList(
        token: String
    ): Response<ExhbtResponseBody>
    suspend fun getExhbtList(
        token: String
    ): Response<ExhbtResponseBody>
    suspend fun getCustomExhbtList(
        token: String
    ): Response<ExhbtResponseBody>
    suspend fun filterDetailExhbtList(
        token: String,
        searchList: String
    ): Response<ExhbtResponseBody>
    suspend fun searchExhbtList(
        token: String,
        words: String
    ): Response<ExhbtResponseBody>

    suspend fun exhbtLikeSave(
        token: String,
        exhbt_cd: String
    ): Response<MsgResponseBody>
    suspend fun exhbtLikeDel(
        token: String,
        exhbt_cd: String
    ): Response<MsgResponseBody>
    suspend fun getExhbtLikeList(
        token: String
    ): Response<ExhbtResponseBody>

    suspend fun exhbtReservationSave(
        token: String,
        exhbt_cd: String
    ): Response<MsgResponseBody>
    suspend fun getExhbtReservationList(
        token: String
    ): Response<ExhbtResponseBody>

    suspend fun getCustomizedList(
        token: String
    ): Response<CustomListResponseBody>
    suspend fun insertCustomizedList(
        token: String,
        answer_idx: String
    ): Response<MsgResponseBody>
    suspend fun deleteCustomizedList(
        token: String
    ): Response<MsgResponseBody>
}