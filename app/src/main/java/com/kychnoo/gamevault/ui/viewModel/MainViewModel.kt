package com.kychnoo.gamevault.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kychnoo.gamevault.R
import com.kychnoo.gamevault.data.model.GameData
import com.kychnoo.gamevault.data.model.RepResult
import com.kychnoo.gamevault.data.model.ui.UiState
import com.kychnoo.gamevault.data.remote.repository.RawgGamesRepository
import com.kychnoo.gamevault.provider.AndroidResourceProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainViewModel(
    private val resourceProvider: AndroidResourceProvider,
    private val gamesRepository: RawgGamesRepository
) : ViewModel() {
    private val _uiState: MutableStateFlow<UiState<List<GameData>>> = MutableStateFlow<UiState<List<GameData>>>(UiState.Loading)
    val uiState: StateFlow<UiState<List<GameData>>> = _uiState.asStateFlow()

    init {
        loadGames()
    }

    fun loadGames() {
        viewModelScope.launch {
            _uiState.value = UiState.Loading

            _uiState.value = when (val result = gamesRepository.fetchAllGames()) {
                is RepResult.Success -> UiState.Success(result.data)
                is RepResult.Error -> UiState.Error(result.exception.message ?: resourceProvider.getString(R.string.unknown_error))
            }
        }
    }
}