package com.limit.lazybird.api

import com.limit.lazybird.models.retrofit.*
import retrofit2.Response

/****************** ApiHelper *********************
 * API 연결에 사용하기 위한 interface 파일
 * 이를 기반으로 ApiHelperImpl 객체를 만든다.
 ********************************************** ***/

interface ApiHelper {
    /********* Calendar *********/
    suspend fun getUnRegistList(
        token: String
    ): CalendarListResponseBody

    suspend fun saveCalendarInfo(
        token: String,
        exhbt_cd: String,
        reser_dt: String,
        start_time: String,
        end_time: String
    ): CodeMsgResponseBody

    suspend fun deleteCalendarInfo(
        token: String,
        exhbt_cd: String
    ): CodeMsgResponseBody

    suspend fun getRegistListAll(
        token: String
    ): CalendarListResponseBody

    suspend fun saveCustomInfo(
        token: String,
        exhbt_nm: String,
        exhbt_lct: String,
        reser_dt: String,
        start_time: String,
        end_time: String
    ): CodeMsgResponseBody

    suspend fun updateCustomInfo(
        token: String,
        exhbt_cd: String,
        exhbt_nm: String,
        exhbt_lct: String,
        reser_dt: String,
        start_time: String,
        end_time: String
    ): CodeMsgResponseBody

    suspend fun delCustomInfo(
        token: String,
        exhbt_cd: String
    ): CodeMsgResponseBody

    suspend fun getCustomListAll(
        token: String
    ): CalendarListResponseBody

    suspend fun visitUpdateExhbt(
        token: String,
        exhbt_nm: String,
        visit_yn: String
    ): CodeMsgResponseBody

    suspend fun visitUpdateCustom(
        token: String,
        exhbt_nm: String,
        visit_yn: String
    ): CodeMsgResponseBody

    /********* Customized Question *********/
    suspend fun getCustomizedList(
        token: String
    ): CustomListResponseBody

    suspend fun insertCustomizedList(
        token: String,
        answer_idx: String
    ): MsgResponseBody

    suspend fun deleteCustomizedList(
        token: String
    ): MsgResponseBody

    /********* Exhibit Info *********/
    suspend fun getNoticeList(
    ): NoticeListResponseBody

    suspend fun getExhbtList(
        token: String
    ): ExhbtResponseBody

    suspend fun getEarlyList(
        token: String
    ): ExhbtResponseBody

    suspend fun getCustomExhbtList(
        token: String
    ): ExhbtResponseBody

    suspend fun filterDetailExhbtList(
        token: String,
        searchList: String
    ): ExhbtResponseBody

    suspend fun searchExhbtList(
        token: String,
        words: String
    ): ExhbtResponseBody


    /********* Exhibit Status *********/
    suspend fun exhbtLikeSave(
        token: String,
        exhbt_cd: String
    ): MsgResponseBody

    suspend fun exhbtLikeDel(
        token: String,
        exhbt_cd: String
    ): MsgResponseBody

    suspend fun getExhbtLikeList(
        token: String
    ): ExhbtResponseBody

    suspend fun exhbtReservationSave(
        token: String,
        exhbt_cd: String
    ): MsgResponseBody

    suspend fun exhbtReservationDelete(
        token: String,
        exhbt_cd: String
    ): CodeMsgResponseBody

    suspend fun getExhbtReservationList(
        token: String
    ): ExhbtResponseBody

    suspend fun getEarlyCardList(
        token: String
    ): EarlyCardResponseBody

    /********* Login *********/
    suspend fun loginKakao(
        token: String
    ): LoginResponseBody

    suspend fun loginGoogle(
        token: String
    ): LoginResponseBody

    suspend fun deleteUser(
        token: String
    ): MemberOutResponseBody
}
