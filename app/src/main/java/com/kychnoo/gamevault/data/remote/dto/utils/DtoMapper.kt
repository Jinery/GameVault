package com.kychnoo.gamevault.data.remote.dto.utils

interface DtoMapper<T> {
    fun toData(): T
}