package com.kychnoo.gamevault.data.model.states.extensions

import com.kychnoo.gamevault.data.model.GameData
import com.kychnoo.gamevault.data.model.ui.UiState
import com.kychnoo.gamevault.data.model.ui.mapData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

fun Flow<UiState<List<GameData>>>.withFavorites(
    favoritesIdsFlow: Flow<List<Int>>
): Flow<UiState<List<GameData>>> = combine(
    this,
    favoritesIdsFlow
) { gamesState, favoriteIds ->
    gamesState.mapData { games ->
        games.map { game ->
            game.copy(isFavorite = favoriteIds.contains(game.id))
        }
    }
}