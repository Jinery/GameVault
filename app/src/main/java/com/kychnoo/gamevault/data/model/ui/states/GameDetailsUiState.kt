package com.kychnoo.gamevault.data.model.ui.states

import com.kychnoo.gamevault.data.model.GameData
import com.kychnoo.gamevault.data.model.development.DevelopmentTeamPageData
import com.kychnoo.gamevault.data.model.gameDetail.GameDetailData
import com.kychnoo.gamevault.data.model.screenshots.ScreenshotData
import com.kychnoo.gamevault.data.model.ui.UiState

data class GameDetailsUiState(
    val gameDetailsState: UiState<GameDetailData> = UiState.Loading, // State for game details.
    val screenshotsState: UiState<ScreenshotData> = UiState.Loading, // State for screenshots for selected game.
    val developmentTeamsState: UiState<DevelopmentTeamPageData> = UiState.Loading, // State for game development teams.
    val suggestedGames: UiState<List<GameData>> = UiState.Loading // State for suggested games.
)
