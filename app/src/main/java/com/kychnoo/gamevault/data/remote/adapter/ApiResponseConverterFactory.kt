package com.kychnoo.gamevault.data.remote.adapter

import com.kychnoo.gamevault.data.remote.dto.response.ApiResponse
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

class ApiResponseConverterFactory : Converter.Factory() {
    override fun responseBodyConverter(
        type: Type,
        annotations: Array<out Annotation>,
        retrofit: Retrofit
    ): Converter<ResponseBody, *>? {
        if (getRawType(type) != ApiResponse::class.java) return null

        val successType = getParameterUpperBound(0, type as ParameterizedType)
        val delegate = retrofit.nextResponseBodyConverter<Any>(this, successType, annotations)

        return Converter<ResponseBody, ApiResponse<Any>> { body ->
            try {
                val data = delegate.convert(body)!!
                ApiResponse.Success(data, 200)
            } catch (e: Exception) {
                throw e // Retrofit сам превратит в HTTP error
            }
        }
    }
}