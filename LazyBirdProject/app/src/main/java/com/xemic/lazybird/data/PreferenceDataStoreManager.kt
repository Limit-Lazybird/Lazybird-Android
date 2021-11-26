package com.xemic.lazybird.data

import android.content.Context
import androidx.datastore.preferences.*
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.xemic.lazybird.models.UserInfo
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PreferenceDataStoreManager @Inject constructor(
    @ApplicationContext applicationContext: Context
) {
    object PreferenceKey {
        val TOKEN = preferencesKey<String>("token")
        val EMAIL = preferencesKey<String>("email")
        val NAME = preferencesKey<String>("name")
    }

    private val dataStore = applicationContext.createDataStore("user_token")

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

    val preferenceUserInfoFlow = dataStore.data
        .catch { exception ->
            if(exception is IOException){
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            val email = preferences[PreferenceKey.EMAIL] ?: ""
            val name = preferences[PreferenceKey.NAME] ?: ""
            UserInfo(email, name)
        }

    suspend fun updateToken(token: String) {
        dataStore.edit { mutablePreferences ->
            mutablePreferences[PreferenceKey.TOKEN] = token
        }
    }

    suspend fun updateUserInfo(email:String, name:String){
        dataStore.edit { mutablePreferences ->
            mutablePreferences[PreferenceKey.EMAIL] = email
            mutablePreferences[PreferenceKey.NAME] = name
        }
    }
}