package com.xemic.lazybird.api

import com.xemic.lazybird.models.Todo
import com.xemic.lazybird.models.retrofit.*
import retrofit2.Response
import javax.inject.Inject

class ApiHelperImpl @Inject constructor(
    private val apiService: ApiService
) : ApiHelper {
    override suspend fun getTodos(): Response<List<Todo>> = apiService.getTodos()
    override suspend fun loginKakao(
        email: String,
        token: String,
        name: String
    ): Response<LoginResponseBody> = apiService.loginKakao(
        "01",
        email,
        token,
        name
    )

    override suspend fun loginGoogle(
        email: String,
        token: String,
        name: String
    ): Response<LoginResponseBody> = apiService.loginGoogle(
        "02",
        email,
        token,
        name
    )

    override suspend fun getEarlyList(
        token: String
    ): Response<ExhbtResponseBody> = apiService.getEarlyList(token)

    override suspend fun getExhbtList(
        token: String
    ): Response<ExhbtResponseBody> = apiService.getExhbtList(token)

    override suspend fun getCustomExhbtList(
        token: String
    ): Response<ExhbtResponseBody> = apiService.getCustomExhbtList(token)

    override suspend fun filterDetailExhbtList(
        token: String,
        searchList: String
    ): Response<ExhbtResponseBody> = apiService.filterDetailExhbtList(token, searchList)

    override suspend fun searchExhbtList(
        token: String,
        words: String
    ): Response<ExhbtResponseBody> = apiService.searchExhbtList(token, words)

    override suspend fun exhbtLikeSave(
        token: String,
        exhbt_cd: String
    ): Response<MsgResponseBody> = apiService.exhbtLikeSave(token, exhbt_cd, "Y")

    override suspend fun exhbtLikeDel(
        token: String,
        exhbt_cd: String
    ): Response<MsgResponseBody> = apiService.exhbtLikeDel(token, exhbt_cd)

    override suspend fun getExhbtLikeList(
        token: String
    ): Response<ExhbtResponseBody> = apiService.getExhbtLikeList(token)

    override suspend fun exhbtReservationSave(
        token: String,
        exhbt_cd: String
    ): Response<MsgResponseBody> = apiService.exhbtReservationSave(token, exhbt_cd, "20")

    override suspend fun getExhbtReservationList(
        token: String
    ): Response<ExhbtResponseBody> = apiService.getExhbtReservationList(token, "20")

    override suspend fun getCustomizedList(
        token: String
    ): Response<CustomListResponseBody> = apiService.getCustomizedList(token)

    override suspend fun insertCustomizedList(
        token: String,
        answer_idx: String
    ): Response<MsgResponseBody> = apiService.insertCustomizedList(token, answer_idx)

    override suspend fun deleteCustomizedList(
        token: String
    ): Response<MsgResponseBody> = apiService.deleteCustomizedList(token)
}