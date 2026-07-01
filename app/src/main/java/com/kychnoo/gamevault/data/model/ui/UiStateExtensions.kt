package com.kychnoo.gamevault.data.model.ui

inline fun <T, R> UiState<T>.mapData(transform: (T) -> R): UiState<R> = when (this) {
    is UiState.Success<T> -> UiState.Success(transform(data))
    is UiState.Error -> UiState.Error(message)
    UiState.Loading -> UiState.Loading
}