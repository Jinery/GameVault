package com.kychnoo.gamevault.data.model

sealed class RepResult<out T> {
    data class Success<T>(val data: T) : RepResult<T>()
    data class Error(val exception: Throwable) : RepResult<Nothing>()
}