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

    override suspend fun saveCalendarInfo(
        token: String,
        exhbt_cd: String,
        reser_dt: String,
        start_time: String,
        end_time: String
    ): Response<CodeMsgResponseBody> = apiService.saveCalendarInfo(token, exhbt_cd, reser_dt, start_time, end_time)

    override suspend fun deleteCalendarInfo(
        token: String,
        exhbt_cd: String
    ): Response<CodeMsgResponseBody> = apiService.deleteCalendarInfo(token, exhbt_cd)

    override suspend fun getRegistListAll(
        token: String
    ): Response<CalendarListResponseBody> = apiService.getRegistListAll(token, "all")

    override suspend fun saveCustomInfo(
        token: String,
        exhbt_nm: String,
        exhbt_lct: String,
        reser_dt: String,
        start_time: String,
        end_time: String
    ): Response<CodeMsgResponseBody> = apiService.saveCustomInfo(token, exhbt_nm, exhbt_lct, reser_dt, start_time, end_time)

    override suspend fun updateCustomInfo(
        token: String,
        exhbt_cd: String,
        exhbt_nm: String,
        exhbt_lct: String,
        reser_dt: String,
        start_time: String,
        end_time: String
    ): Response<CodeMsgResponseBody> = apiService.updateCustomInfo(token, exhbt_cd, exhbt_nm, exhbt_lct, reser_dt, start_time, end_time)

    override suspend fun delCustomInfo(
        token: String,
        exhbt_cd: String
    ): Response<CodeMsgResponseBody> = apiService.delCustomInfo(token, exhbt_cd)


    override suspend fun getCustomListAll(
        token: String
    ): Response<CalendarListResponseBody> = apiService.getCustomListAll(token, "all")

    override suspend fun visitUpdateExhbt(
        token: String,
        exhbt_nm: String,
        visit_yn: String
    ): Response<CodeMsgResponseBody> = apiService.visitUpdateExhbt(token, exhbt_nm, visit_yn)

    override suspend fun visitUpdateCustom(
        token: String,
        exhbt_nm: String,
        visit_yn: String
    ): Response<CodeMsgResponseBody> = apiService.visitUpdateCustom(token, exhbt_nm, visit_yn, "custom")

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

    /********* Etc *********/

    override suspend fun getNoticeList(): Response<NoticeListResponseBody> = apiService.getNoticeList()

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

    override suspend fun exhbtReservationDelete(
        token: String,
        exhbt_cd: String
    ): Response<CodeMsgResponseBody>  = apiService.exhbtReservationDelete(token, exhbt_cd)

    override suspend fun getExhbtReservationList(
        token: String
    ): Response<ExhbtResponseBody> = apiService.getExhbtReservationList(token, "20")
    override suspend fun getEarlyCardList(
        token: String
    ): Response<EarlyCardResponseBody> = apiService.getEarlyCardList(token)

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