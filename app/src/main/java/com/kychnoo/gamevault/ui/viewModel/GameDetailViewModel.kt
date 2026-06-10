package com.kychnoo.gamevault.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kychnoo.gamevault.R
import com.kychnoo.gamevault.data.model.RepResult
import com.kychnoo.gamevault.data.model.development.DevelopmentTeamPageData
import com.kychnoo.gamevault.data.model.gameDetail.GameDetailData
import com.kychnoo.gamevault.data.model.screenshots.ScreenshotData
import com.kychnoo.gamevault.data.model.ui.UiState
import com.kychnoo.gamevault.data.model.ui.states.GameDetailsUiState
import com.kychnoo.gamevault.data.remote.repository.RawgDetailGamesRepository
import com.kychnoo.gamevault.data.remote.repository.RawgDevelopmentTeamsRepository
import com.kychnoo.gamevault.data.remote.repository.RawgScreenshotsRepository
import com.kychnoo.gamevault.provider.AndroidResourceProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class GameDetailViewModel(
    private val gameDetailRepository: RawgDetailGamesRepository,
    private val screenshotsRepository: RawgScreenshotsRepository,
    private val developmentTeamsRepository: RawgDevelopmentTeamsRepository,
    private val resourceProvider: AndroidResourceProvider
) : ViewModel() {

    private val _uiState: MutableStateFlow<GameDetailsUiState> = MutableStateFlow(GameDetailsUiState())
    val uiState: StateFlow<GameDetailsUiState> = _uiState.asStateFlow()

    fun getGameDetails(gameId: Int) {
        val currentDetails = _uiState.value.gameDetailsState

        if (currentDetails is UiState.Success && currentDetails.data.id == gameId) return // Return if current details status is success.

        viewModelScope.launch {
            _uiState.update { it.copy(gameDetailsState = UiState.Loading) } // Set loading status for game details state.

            val newState = when (val result = gameDetailRepository.getGameDetails(gameId)) { // Assigning the state retrieved from the repository to a variable.
                is RepResult.Success<GameDetailData> -> UiState.Success(result.data)
                is RepResult.Error -> UiState.Error(result.exception.message ?: resourceProvider.getString(R.string.unknown_error))
            }

            _uiState.update { it.copy(gameDetailsState = newState) } // Update current game details state.
        }
    }

    fun getGameScreenshots(gameId: Int) {
        val currentScreenshotsState = _uiState.value.screenshotsState

        if (currentScreenshotsState is UiState.Success && currentScreenshotsState.data.gameId == gameId) return // Return if current screenshots state status is success.

        viewModelScope.launch {
            _uiState.update { it.copy(screenshotsState = UiState.Loading) } // Set loading status for screenshots state.

            val newState = when (val result = screenshotsRepository.getGameScreenshots(gameId)) { // Assigning the state retrieved from the repository to a variable.
                is RepResult.Success<ScreenshotData> -> UiState.Success(result.data)
                is RepResult.Error -> UiState.Error(result.exception.message ?: resourceProvider.getString(R.string.unknown_error))
            }

            _uiState.update { it.copy(screenshotsState = newState) }
        }
    }

    fun getDevelopmentTeamsForGame(gameId: Int) {
        val currentDevelopmentTeamsState = _uiState.value.developmentTeamsState

        if (currentDevelopmentTeamsState is UiState.Success && currentDevelopmentTeamsState.data.gameId == gameId) return

        viewModelScope.launch {
            _uiState.update { it.copy(developmentTeamsState = UiState.Loading) }

            val newState = when (val result = developmentTeamsRepository.getDevelopmentTeamForGame(gameId)) {
                is RepResult.Success<DevelopmentTeamPageData> -> UiState.Success(result.data)
                is RepResult.Error -> UiState.Error(result.exception.message ?: resourceProvider.getString(R.string.unknown_error))
            }

            _uiState.update { it.copy(developmentTeamsState = newState) }
        }
    }

    fun loadGameData(gameId: Int) {
        getGameDetails(gameId)
        getGameScreenshots(gameId)
        getDevelopmentTeamsForGame(gameId)
    }
}