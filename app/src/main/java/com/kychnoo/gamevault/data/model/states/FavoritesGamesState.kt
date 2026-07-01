package com.kychnoo.gamevault.data.model.states

import com.kychnoo.gamevault.data.model.GameData

data class FavoritesGamesState(
    val games: List<GameData>,
    val errorMessage: String?,
    val missingIds: List<Int> = emptyList(),
)
