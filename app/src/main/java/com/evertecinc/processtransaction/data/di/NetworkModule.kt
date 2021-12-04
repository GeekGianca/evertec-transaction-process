package com.evertecinc.processtransaction.data.di

import com.evertecinc.processtransaction.BuildConfig
import com.evertecinc.processtransaction.domain.client.PlaceToPayApi
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Singleton
    @Provides
    fun provideOkHttpClient(): OkHttpClient.Builder =
        OkHttpClient.Builder().connectTimeout(120, TimeUnit.SECONDS)
            .readTimeout(120, TimeUnit.SECONDS)

    @Singleton
    @Provides
    fun provideLoggingHeaders(): HttpLoggingInterceptor = HttpLoggingInterceptor()
        .setLevel(HttpLoggingInterceptor.Level.HEADERS)
        .setLevel(HttpLoggingInterceptor.Level.BODY)

    @Singleton
    @Provides
    fun provideRetrofit(
        okHttp: OkHttpClient.Builder,
        interceptor: HttpLoggingInterceptor
    ): Retrofit.Builder =
        Retrofit
            .Builder()
            .baseUrl(BuildConfig.BASE_URL)// Inject by BuildConfig or String resources
            .client(okHttp.addInterceptor(interceptor).build())
            .addConverterFactory(GsonConverterFactory.create())

    @Singleton
    @Provides
    fun providePlaceToPayApi(retrofit: Retrofit.Builder): PlaceToPayApi =
        retrofit
            .build()
            .create(PlaceToPayApi::class.java)

}