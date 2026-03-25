package com.kychnoo.gamevault.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kychnoo.gamevault.R
import com.kychnoo.gamevault.data.model.RepResult
import com.kychnoo.gamevault.data.model.gameDetail.GameDetailData
import com.kychnoo.gamevault.data.model.ui.UiState
import com.kychnoo.gamevault.data.remote.repository.RawgDetailGamesRepository
import com.kychnoo.gamevault.provider.AndroidResourceProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class GameDetailViewModel(
    private val gameDetailRepository: RawgDetailGamesRepository,
    private val resourceProvider: AndroidResourceProvider
) : ViewModel() {

    private val _uiState: MutableStateFlow<UiState<GameDetailData>> = MutableStateFlow<UiState<GameDetailData>>(UiState.Loading)
    val uiState: StateFlow<UiState<GameDetailData>> = _uiState.asStateFlow()

    fun getGameDetails(gameId: Int) {
        viewModelScope.launch {
            _uiState.value = UiState.Loading

            _uiState.value = when (val result = gameDetailRepository.getGameDetails(gameId)) {
                is RepResult.Success<GameDetailData> -> UiState.Success(result.data)
                is RepResult.Error -> UiState.Error(result.exception.message ?: resourceProvider.getString(R.string.unknown_error))
            }
        }
    }
}