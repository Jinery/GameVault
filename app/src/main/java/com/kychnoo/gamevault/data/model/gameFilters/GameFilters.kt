package com.kychnoo.gamevault.data.model.gameFilters

import androidx.compose.runtime.Immutable

@Immutable
data class GameFilters(
    val ordering: String = "-added",
    val selectedGenres: List<Int> = emptyList()
)