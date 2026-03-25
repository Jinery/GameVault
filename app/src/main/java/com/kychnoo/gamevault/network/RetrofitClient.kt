package com.kychnoo.gamevault.network

import com.kychnoo.gamevault.BuildConfig
import com.kychnoo.gamevault.data.remote.adapter.ApiResponseAdapterFactory
import com.kychnoo.gamevault.data.remote.api.RawgApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

object RetrofitClient {
    private const val BASE_URL: String = "https://api.rawg.io/api/"
    private const val API_KEY: String = BuildConfig.RAWG_API_KEY

    val json: Json = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
        isLenient = true
    }

    private val okHttpClient: OkHttpClient = OkHttpClient.Builder()
        .addInterceptor { chain ->
            var original = chain.request()
            val originalUrl = original.url

            val updatedUrl = originalUrl.newBuilder()
                .addQueryParameter("key", API_KEY)
                .build()

            val updatedRequest = original.newBuilder()
                .url(updatedUrl)
                .build()

            chain.proceed(updatedRequest)
        }
        .build()

    val api: RawgApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addCallAdapterFactory(ApiResponseAdapterFactory())
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
            .create(RawgApi::class.java)
    }
}