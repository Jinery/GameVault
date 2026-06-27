package com.kychnoo.gamevault.data.model.ui.states

import com.kychnoo.gamevault.data.local.entity.SearchHistoryEntity
import com.kychnoo.gamevault.data.model.GameData
import com.kychnoo.gamevault.data.model.gameFilters.GameFilters
import com.kychnoo.gamevault.data.model.genres.GenreData
import com.kychnoo.gamevault.data.model.ui.UiState

data class SearchUiState(
    val searchQuery: String = "",
    val searchHistory: List<SearchHistoryEntity> = emptyList(),
    val games: UiState<List<GameData>> = UiState.Success(emptyList()),
    val availableGenres: UiState<List<GenreData>> = UiState.Loading,
    val filters: GameFilters = GameFilters()
)
