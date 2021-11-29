package com.xemic.lazybird.data

import android.content.Context
import androidx.datastore.preferences.*
import com.xemic.lazybird.models.UserInfo
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

/*********** PreferenceDataStoreManager ***********
 * Preference DataStore 를 사용하기 위핸 Manager 객체이다.
 * Signleton으로 제공된다.
 * Todo : AppModule 에 추가하기
 * Todo : DataStore 이름 바꿔주기 (user_token >> user_datastore)
 ********************************************** ***/
@Singleton
class PreferenceDataStoreManager @Inject constructor(
    @ApplicationContext applicationContext: Context
) {
    object PreferenceKey {
        val TOKEN = preferencesKey<String>("token") // 사용자 토큰 정보
        val EMAIL = preferencesKey<String>("email") // 사용자의 이메일
        val NAME = preferencesKey<String>("name") // 사용자의 이름
        val LOGIN_TYPE = preferencesKey<String>("login_type") // 사용자의 이름
    }

    private val dataStore = applicationContext.createDataStore("user_token")

    // Token 제공받는 Flow
    val preferenceTokenFlow = dataStore.data
        .catch { exception ->
            if(exception is IOException){
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            val token = preferences[PreferenceKey.TOKEN] ?: ""
            token
        }

    // UserInfo(email, name) 제공받는 Flow
    val preferenceUserInfoFlow = dataStore.data
        .catch { exception ->
            if(exception is IOException){
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            val loginType = preferences[PreferenceKey.LOGIN_TYPE] ?: ""
            val email = preferences[PreferenceKey.EMAIL] ?: ""
            val name = preferences[PreferenceKey.NAME] ?: ""
            UserInfo(loginType, email, name)
        }

    // Token 정보 업데이트
    suspend fun updateToken(token: String) {
        dataStore.edit { mutablePreferences ->
            mutablePreferences[PreferenceKey.TOKEN] = token
        }
    }

    // UserInfo(email, name) 정보 업데이트
    suspend fun updateUserInfo(loginType:String, email:String, name:String){
        dataStore.edit { mutablePreferences ->
            mutablePreferences[PreferenceKey.LOGIN_TYPE] = loginType
            mutablePreferences[PreferenceKey.EMAIL] = email
            mutablePreferences[PreferenceKey.NAME] = name
        }
    }
}