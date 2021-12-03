package com.limit.lazybird.api

import com.limit.lazybird.models.retrofit.*
import retrofit2.Response
import javax.inject.Inject

/****************** ApiHelperImpl *********************
 * API 연결에 사용하기 위한 객체
 * AppModule 파일을 통해 각각의 class에 DI Container로 Injection 된다.
 ********************************************** ***/

class ApiHelperImpl @Inject constructor(
    private val apiService: ApiService
) : ApiHelper {

    /********* Calendar *********/
    override suspend fun getUnRegistList(
        token: String
    ): Response<CalendarListResponseBody> = apiService.getUnRegistList(token)

    /********* Customized Question *********/
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

    /********* Exhibit Info *********/
    override suspend fun getExhbtList(
        token: String
    ): Response<ExhbtResponseBody> = apiService.getExhbtList(token)
    override suspend fun getEarlyList(
        token: String
    ): Response<ExhbtResponseBody> = apiService.getEarlyList(token)
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

    /********* Exhibit Status *********/
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

    /********* Login *********/
    override suspend fun loginKakao(
        token: String
    ): Response<LoginResponseBody> = apiService.login(
        "01",
        token
    )
    override suspend fun loginGoogle(
        token: String
    ): Response<LoginResponseBody> = apiService.login(
        "02",
        token
    )
    override suspend fun deleteUser(
        token: String
    ): Response<MemberOutResponseBody> = apiService.deleteUser(
        token
    )
}