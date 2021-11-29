package com.limit.lazybird.api

import com.limit.lazybird.models.retrofit.*
import retrofit2.Response

/****************** ApiHelper *********************
 * API 연결에 사용하기 위한 interface 파일
 * 이를 기반으로 ApiHelperImpl 객체를 만든다.
********************************************** ***/

interface ApiHelper {
    /********* Customized Question *********/
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


    /********* Exhibit Info *********/
    suspend fun getExhbtList(
        token: String
    ): Response<ExhbtResponseBody>

    suspend fun getEarlyList(
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


    /********* Exhibit Status *********/
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


    /********* Login *********/
    suspend fun loginKakao(
        token: String
    ): Response<LoginResponseBody>

    suspend fun loginGoogle(
        token: String
    ): Response<LoginResponseBody>
}