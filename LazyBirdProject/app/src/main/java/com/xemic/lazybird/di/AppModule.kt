package com.xemic.lazybird.di

import com.google.gson.GsonBuilder
import com.xemic.lazybird.api.ApiHelper
import com.xemic.lazybird.api.ApiHelperImpl
import com.xemic.lazybird.api.ApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

/****************** AppModule *********************
 * Injection 을 위한 제공자들이 모여있는 Module
 * Todo : Preference DataStore 도 여기에 추가하기
 ********************************************** ***/
@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Singleton
    @Provides
    fun provideRetrofit(): Retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
        .baseUrl("https://limit-lazybird.com")
        .build()

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): ApiService = retrofit.create(ApiService::class.java)

    @Provides
    @Singleton
    fun provideApiHelper(apiHelper: ApiHelperImpl): ApiHelper = apiHelper
}