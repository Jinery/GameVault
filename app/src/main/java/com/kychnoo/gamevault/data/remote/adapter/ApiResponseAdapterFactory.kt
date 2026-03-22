package com.kychnoo.gamevault.data.remote.adapter

import com.kychnoo.gamevault.data.remote.dto.response.ApiResponse
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Retrofit
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

class ApiResponseAdapterFactory : CallAdapter.Factory() {
    override fun get(
        returnType: Type,
        annotations: Array<out Annotation>,
        retrofit: Retrofit
    ): CallAdapter<*, *>? {
        if (getRawType(returnType) != Call::class.java) return null

        val callType = getParameterUpperBound(0, returnType as ParameterizedType)
        if (getRawType(callType) != ApiResponse::class.java) return null

        val responseType = getParameterUpperBound(0, callType as ParameterizedType)

        return object : CallAdapter<Any, Call<ApiResponse<Any>>> {
            override fun responseType(): Type = responseType

            override fun adapt(call: Call<Any>): Call<ApiResponse<Any>> {
                return ApiResponseCall(call, responseType)
            }
        }
    }
}