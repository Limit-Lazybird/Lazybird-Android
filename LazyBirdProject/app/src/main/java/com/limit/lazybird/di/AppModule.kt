package com.limit.lazybird.di

import android.content.Context
import com.google.gson.GsonBuilder
import com.limit.lazybird.BuildConfig
import com.limit.lazybird.api.ApiHelper
import com.limit.lazybird.api.ApiHelperImpl
import com.limit.lazybird.api.ApiService
import com.limit.lazybird.datastore.PreferenceDataStoreManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

/****************** AppModule *********************
 * Injection 을 위한 제공자들이 모여있는 Module
 ********************************************** ***/
@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
        .baseUrl(BuildConfig.LAZYBIRD_SERVER_URL)
        .build()

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): ApiService = retrofit.create(ApiService::class.java)

    @Provides
    @Singleton
    fun provideApiHelper(apiHelper: ApiHelperImpl): ApiHelper = apiHelper

    @Provides
    @Singleton
    fun providePreferenceDataStoreManager(
        @ApplicationContext context: Context
    ): PreferenceDataStoreManager =
        PreferenceDataStoreManager(context)
}