package com.kychnoo.gamevault.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kychnoo.gamevault.R
import com.kychnoo.gamevault.data.manager.favorites.FavoritesManager
import com.kychnoo.gamevault.data.model.GameData
import com.kychnoo.gamevault.data.model.RepResult
import com.kychnoo.gamevault.data.model.states.extensions.withFavorites
import com.kychnoo.gamevault.data.model.ui.UiState
import com.kychnoo.gamevault.data.remote.repository.RawgGamesRepository
import com.kychnoo.gamevault.provider.AndroidResourceProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MainViewModel(
    private val resourceProvider: AndroidResourceProvider,
    private val gamesRepository: RawgGamesRepository,
    favoritesManager: FavoritesManager
) : ViewModel(), FavoritesManager by favoritesManager {
    private val _gamesFlow: MutableStateFlow<UiState<List<GameData>>> = MutableStateFlow(UiState.Loading)

    val uiState: StateFlow<UiState<List<GameData>>> = _gamesFlow
        .withFavorites(getFavoriteIds())
        .stateIn(scope = viewModelScope, started = SharingStarted.WhileSubscribed(5000), initialValue = UiState.Loading)

    init {
        loadGames()
    }

    fun loadGames() {
        viewModelScope.launch {
            _gamesFlow.update { UiState.Loading }

            val newState = when (val result = gamesRepository.fetchAllGames()) {
                is RepResult.Success -> UiState.Success(result.data)
                is RepResult.Error -> UiState.Error(result.exception.message ?: resourceProvider.getString(R.string.unknown_error))
            }

            _gamesFlow.update { newState }
        }
    }
}