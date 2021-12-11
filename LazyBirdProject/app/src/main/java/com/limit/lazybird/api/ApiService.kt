package com.limit.lazybird.api

import com.limit.lazybird.models.retrofit.*
import retrofit2.Response
import retrofit2.http.*

/****************** ApiService *********************
 * Retrofit Service interface 파일
 ********************************************** ***/

interface ApiService {
    /********* Calendar *********/
    @FormUrlEncoded
    @POST("/calender/unRegistList")
    suspend fun getUnRegistList(
        @Field("token") token: String
    ):Response<CalendarListResponseBody>

    @FormUrlEncoded
    @POST("/calender/infoSave")
    suspend fun saveCalendarInfo(
        @Field("token") token: String,
        @Field("exhbt_cd") exhbt_cd: String,
        @Field("reser_dt") reser_dt: String,
        @Field("start_time") start_time: String,
        @Field("end_time") end_time: String,
    ):Response<CodeMsgResponseBody>

    @FormUrlEncoded
    @POST("/calender/infoSave")
    suspend fun deleteCalendarInfo(
        @Field("token") token: String,
        @Field("exhbt_cd") exhbt_cd: String
    ):Response<CodeMsgResponseBody>

    @FormUrlEncoded
    @POST("/calender/registList")
    suspend fun getRegistListAll(
        @Field("token") token: String,
        @Field("reser_dt") reser_dt: String
    ):Response<CalendarListResponseBody>

    @FormUrlEncoded
    @POST("/calender/customInfoSave")
    suspend fun saveCustomInfo(
        @Field("token") token: String,
        @Field("exhbt_nm") exhbt_nm: String,
        @Field("exhbt_lct") exhbt_lct: String,
        @Field("reser_dt") reser_dt: String,
        @Field("start_time") start_time: String,
        @Field("end_time") end_time: String,
    ):Response<CodeMsgResponseBody>

    @FormUrlEncoded
    @POST("/calender/customInfoDel")
    suspend fun delCustomInfo(
        @Field("token") token: String,
        @Field("exhbt_cd") exhbt_nm: String
    ):Response<CodeMsgResponseBody>

    @FormUrlEncoded
    @POST("/calender/visitUpdate")
    suspend fun visitUpdateExhbt(
        @Field("token") token: String,
        @Field("exhbt_cd") exhbt_nm: String,
        @Field("visit_yn") visit_yn: String
    ):Response<CodeMsgResponseBody>

    @FormUrlEncoded
    @POST("/calender/visitUpdate")
    suspend fun visitUpdateCustom(
        @Field("token") token: String,
        @Field("exhbt_cd") exhbt_nm: String,
        @Field("visit_yn") visit_yn: String,
        @Field("exhbt_type") exhbt_type: String
    ):Response<CodeMsgResponseBody>

    @FormUrlEncoded
    @POST("/calender/registCustomList")
    suspend fun getCustomListAll(
        @Field("token") token: String,
        @Field("reser_dt") reser_dt: String
    ):Response<CalendarListResponseBody>

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

    /********* Etc *********/
    @GET("/notice/list")
    suspend fun getNoticeList(
    ):Response<NoticeListResponseBody>

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

    @FormUrlEncoded
    @POST("/status/earlyCardList")
    suspend fun getEarlyCardList(
        @Field("token") token: String
    ):Response<EarlyCardResponseBody>

    /********* Login *********/
    @FormUrlEncoded
    @POST("/oauth/login")
    suspend fun login(
        @Field("comp_cd") comp_cd: String,
        @Field("token") token: String
    ):Response<LoginResponseBody>

    @FormUrlEncoded
    @POST("/oauth/deleteUser")
    suspend fun deleteUser(
        @Field("token") token: String
    ):Response<MemberOutResponseBody>
}