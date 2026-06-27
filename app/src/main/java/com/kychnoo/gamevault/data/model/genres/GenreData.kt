package com.kychnoo.gamevault.data.model.genres

import androidx.compose.runtime.Immutable

@Immutable
data class GenreData(
    val id: Int,
    val name: String,
    val slug: String,
)