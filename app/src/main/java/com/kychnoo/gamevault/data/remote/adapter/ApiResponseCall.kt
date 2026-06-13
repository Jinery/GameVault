package com.kychnoo.gamevault.data.remote.adapter

import com.kychnoo.gamevault.data.remote.dto.response.ApiResponse
import okhttp3.Request
import okio.Timeout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.reflect.Type


class ApiResponseCall(
    private val delegate: Call<Any>,
    private val responseType: Type
) : Call<ApiResponse<Any>> {
    override fun execute(): Response<ApiResponse<Any>> = throw NotImplementedError()

    override fun enqueue(callback: Callback<ApiResponse<Any>>) {
        delegate.enqueue(object : Callback<Any> {
            override fun onResponse(
                call: Call<Any>,
                response: Response<Any>
            ) {
                val apiResponse = when {
                    response.isSuccessful -> {
                        val body = response.body()

                        if (body == null) {
                            ApiResponse.ApiError("Empty body", response.code())
                        } else {
                            ApiResponse.Success(body, response.code())
                        }
                    }
                    else -> {
                        ApiResponse.ApiError(response.errorBody()?.string() ?: "Unknown error", response.code())
                    }
                }
                callback.onResponse(this@ApiResponseCall, Response.success(apiResponse))
            }

            override fun onFailure(call: Call<Any>, throwable: Throwable) {
                val networkError = ApiResponse.NetworkError(throwable)
                callback.onResponse(this@ApiResponseCall, Response.success(networkError))
            }

        })
    }

    override fun isExecuted(): Boolean = delegate.isExecuted

    override fun cancel() = delegate.cancel()

    override fun isCanceled(): Boolean = delegate.isCanceled

    override fun clone(): Call<ApiResponse<Any>> = ApiResponseCall(delegate.clone(), responseType)

    override fun request(): Request = delegate.request()

    override fun timeout(): Timeout = delegate.timeout()
}