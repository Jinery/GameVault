package com.kychnoo.gamevault.data.remote.dto.response

sealed class ApiResponse<out T> {
    data class Success<T>(val data: T, val responseCode: Int) : ApiResponse<T>()
    data class ApiError(val message: String? = null, val responseCode: Int) : ApiResponse<Nothing>()
    data class NetworkError(val throwable: Throwable) : ApiResponse<Nothing>()
    object Loading : ApiResponse<Nothing>()
}