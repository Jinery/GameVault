package com.kychnoo.gamevault.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kychnoo.gamevault.R
import com.kychnoo.gamevault.data.manager.favorites.FavoritesManager
import com.kychnoo.gamevault.data.manager.snackbar.SnackbarManager
import com.kychnoo.gamevault.data.model.GameData
import com.kychnoo.gamevault.data.model.RepResult
import com.kychnoo.gamevault.data.model.states.FavoritesGamesState
import com.kychnoo.gamevault.data.model.types.snackbar.SnackbarTypes
import com.kychnoo.gamevault.data.model.ui.UiState
import com.kychnoo.gamevault.data.remote.repository.FavoriteGamesRepository
import com.kychnoo.gamevault.provider.AndroidResourceProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class FavoritesViewModel(
    private val favoriteGamesRepository: FavoriteGamesRepository,
    private val resourceProvider: AndroidResourceProvider,
    favoritesManager: FavoritesManager,
    snackbarManager: SnackbarManager
) : ViewModel(), FavoritesManager by favoritesManager, SnackbarManager by snackbarManager {
    private val _uiState: MutableStateFlow<UiState<List<GameData>>> = MutableStateFlow(UiState.Loading)
    val uiState: StateFlow<UiState<List<GameData>>> = _uiState.asStateFlow()

    private var isInitialLoad = true

    init {
        observeFavorites()
        loadFavorites()
    }

    private fun observeFavorites() {
        viewModelScope.launch {
            favoriteGamesRepository.getFavoriteGamesStateFlow()
                .collectLatest { state ->
                    if (isInitialLoad && state.games.isEmpty() && state.missingIds.isNotEmpty()) {
                        return@collectLatest
                    }
                    _uiState.update { UiState.Success(state.games) }
                    isInitialLoad = false

                    if (state.missingIds.isNotEmpty()) {
                        loadMissingGames(state.missingIds)
                    }
                }
        }
    }

    private fun loadMissingGames(missingIds: List<Int>) {
        viewModelScope.launch {
            when (val result = favoriteGamesRepository.fetchGamesByIds(missingIds)) {
                is RepResult.Success -> {
                    // Used only for debug.

//                    showSnackbar(
//                        "Loaded ${result.data.size} games",
//                        SnackbarTypes.INFO
//                    )
                }
                is RepResult.Error -> {
                    showSnackbar(
                        resourceProvider.getString(R.string.failed_to_load_favorites, listOf(result.exception.message)),
                        SnackbarTypes.ERROR
                    )
                }
            }
        }
    }

    fun loadFavorites(forceRefresh: Boolean = false) {
        viewModelScope.launch {
            _uiState.update { UiState.Loading }

            when (val result = favoriteGamesRepository.fetchFavoritesGames(forceRefresh)) {
                is RepResult.Success<FavoritesGamesState> -> {
                    _uiState.update { UiState.Success(result.data.games) }
                    result.data.errorMessage?.let {
                        showSnackbar(it, SnackbarTypes.ERROR)
                    }
                }
                is RepResult.Error -> {
                    _uiState.update { UiState.Error(result.exception.message ?: resourceProvider.getString(R.string.unexpected_state)) }
                }
            }
        }
    }
}