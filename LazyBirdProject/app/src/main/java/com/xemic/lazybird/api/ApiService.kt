package com.xemic.lazybird.api

import com.xemic.lazybird.models.retrofit.*
import retrofit2.Response
import retrofit2.http.*

/****************** ApiService *********************
 * Retrofit Service interface 파일
 ********************************************** ***/

interface ApiService {
    /********* Customized Question *********/
    @FormUrlEncoded
    @POST("/customized/list")
    suspend fun getCustomizedList(
        @Field("token") token: String
    ):Response<CustomListResponseBody>

    @FormUrlEncoded
    @POST("/customized/listSave")
    suspend fun insertCustomizedList(
        @Field("token") token: String,
        @Field("answer_idx") answer_idx: String
    ):Response<MsgResponseBody>

    @FormUrlEncoded
    @POST("/customized/listDelete")
    suspend fun deleteCustomizedList(
        @Field("token") token: String
    ):Response<MsgResponseBody>


    /********* Exhibit Info *********/
    @FormUrlEncoded
    @POST("/exhibit/list")
    suspend fun getExhbtList(
        @Field("token") token: String
    ):Response<ExhbtResponseBody>

    @FormUrlEncoded
    @POST("/exhibit/earlyList")
    suspend fun getEarlyList(
        @Field("token") token: String
    ):Response<ExhbtResponseBody>

    @FormUrlEncoded
    @POST("/exhibit/customList")
    suspend fun getCustomExhbtList(
        @Field("token") token: String
    ):Response<ExhbtResponseBody>

    @FormUrlEncoded
    @POST("/exhibit/detailList")
    suspend fun filterDetailExhbtList(
        @Field("token") token: String,
        @Field("searchList") searchList: String
    ):Response<ExhbtResponseBody>

    @FormUrlEncoded
    @POST("/exhibit/searchList")
    suspend fun searchExhbtList(
        @Field("token") token: String,
        @Field("words") words: String
    ):Response<ExhbtResponseBody>


    /********* Exhibit Status *********/
    @FormUrlEncoded
    @POST("/status/likeSave")
    suspend fun exhbtLikeSave(
        @Field("token") token: String,
        @Field("exhbt_cd") exhbt_cd: String,
        @Field("like_yn") like_yn: String
    ):Response<MsgResponseBody>

    @FormUrlEncoded
    @POST("/status/likeDel")
    suspend fun exhbtLikeDel(
        @Field("token") token: String,
        @Field("exhbt_cd") exhbt_cd: String
    ):Response<MsgResponseBody>

    @FormUrlEncoded
    @POST("/status/likeList")
    suspend fun getExhbtLikeList(
        @Field("token") token: String
    ):Response<ExhbtResponseBody>

    @FormUrlEncoded
    @POST("/status/reservationSave")
    suspend fun exhbtReservationSave(
        @Field("token") token: String,
        @Field("exhbt_cd") exhbt_cd: String,
        @Field("state_cd") state_cd: String
    ):Response<MsgResponseBody>

    @FormUrlEncoded
    @POST("/status/reservationList")
    suspend fun getExhbtReservationList(
        @Field("token") token: String,
        @Field("state_cd") state_cd: String
    ):Response<ExhbtResponseBody>


    /********* Login *********/
    @FormUrlEncoded
    @POST("/oauth/login/kakao")
    suspend fun loginKakao(
        @Field("comp_cd") comp_cd: String,
        @Field("token") token: String,
        @Field("name") name: String
    ):Response<LoginResponseBody>

    @FormUrlEncoded
    @POST("/oauth/login/google")
    suspend fun loginGoogle(
        @Field("comp_cd") comp_cd: String,
        @Field("token") token: String,
        @Field("name") name: String
    ):Response<LoginResponseBody>
}