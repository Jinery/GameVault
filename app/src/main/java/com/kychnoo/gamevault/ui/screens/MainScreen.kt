package com.kychnoo.gamevault.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import com.kychnoo.gamevault.R
import com.kychnoo.gamevault.data.model.GameData
import com.kychnoo.gamevault.data.model.platform.PlatformArData
import com.kychnoo.gamevault.data.model.platform.PlatformFamily
import com.kychnoo.gamevault.data.model.ui.UiState
import com.kychnoo.gamevault.ui.theme.GameVaultTheme
import com.kychnoo.gamevault.ui.viewModel.MainViewModel
import com.kychnoo.gamevault.ui.widgets.GameCard
import com.kychnoo.gamevault.ui.widgets.loading.CircularLoader
import kotlinx.serialization.Serializable
import org.koin.androidx.compose.koinViewModel

@Serializable
object MainScreenRoute

@Composable
fun MainScreen(
    onDetailClick: (GameData) -> Unit,
    innerPadding: PaddingValues,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    modifier: Modifier = Modifier,
    viewModel: MainViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    MainScreenContent(
        uiState = uiState,
        onDetailClick = onDetailClick,
        innerPadding = innerPadding,
        sharedTransitionScope = sharedTransitionScope,
        animatedVisibilityScope = animatedVisibilityScope,
        onRetry = { viewModel.loadGames() },
        onFavoriteClick = { id, gameName, isFavorite ->
            viewModel.launchToggleFavorite(viewModel.viewModelScope, id, gameName, isFavorite)
        },
        modifier = modifier
    )
}

@Composable
private fun MainScreenContent(
    uiState: UiState<List<GameData>>,
    onDetailClick: (GameData) -> Unit,
    onFavoriteClick: (id: Int, name: String, isFavorite: Boolean) -> Unit,
    innerPadding: PaddingValues,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        when (uiState) {
            is UiState.Loading -> CircularLoader()

            is UiState.Success -> GamesGrid(
                games = uiState.data,
                innerPadding = innerPadding,
                onDetailClick = onDetailClick,
                onFavoriteClick = onFavoriteClick,
                sharedTransitionScope = sharedTransitionScope,
                animatedVisibilityScope = animatedVisibilityScope
            )

            is UiState.Error -> ErrorMessage(
                message = uiState.message,
                onRetry = onRetry
            )
        }
    }
}

@Composable
fun GamesGrid(
    games: List<GameData>,
    innerPadding: PaddingValues,
    onDetailClick: (GameData) -> Unit,
    onFavoriteClick: (id: Int, name: String, isFavorite: Boolean) -> Unit,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    modifier: Modifier = Modifier,
    cardsModifier: Modifier = Modifier,
    animateCards: Boolean = false
) {
    var isDataLoaded by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(games) {
        if (games.isNotEmpty() && !isDataLoaded) {
            isDataLoaded = true
        }
    }

    AnimatedVisibility(
        visible = isDataLoaded,
        enter = fadeIn(tween(500)) + slideInVertically(
            initialOffsetY = { it / 2 },
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessMediumLow
            )
        )
    ) {
        LazyVerticalGrid(
            columns = GridCells.Adaptive(140.dp),
            modifier = modifier.fillMaxSize(),
            contentPadding = PaddingValues(
                start = 16.dp,
                end = 16.dp,
                top = innerPadding.calculateTopPadding() + 16.dp,
                bottom = innerPadding.calculateBottomPadding() + 16.dp
            ),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(
                items = games,
                key = { game -> game.id }
            ) { game ->
                val cModifier = if (animateCards) {
                    cardsModifier.animateItem(
                        fadeInSpec = tween(300),
                        fadeOutSpec = tween(300),
                        placementSpec = spring(stiffness = Spring.StiffnessMediumLow)
                    )
                } else {
                    cardsModifier
                }
                GameCard(
                    gameData = game,
                    onCardClick = { onDetailClick(game) },
                    onFavoriteClick = { onFavoriteClick(game.id, game.title, game.isFavorite) },
                    sharedTransitionScope = sharedTransitionScope,
                    animatedVisibilityScope = animatedVisibilityScope,
                    modifier = cModifier
                )
            }
        }
    }
}

@Composable
fun ErrorMessage(
    message: String,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = stringResource(R.string.error_header),
            style = MaterialTheme.typography.headlineMedium
        )
        Text(
            text = message,
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center
        )
        Button(onClick = onRetry) {
            Text(stringResource(R.string.retry))
        }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Preview(showBackground = true)
@Composable
private fun MainScreenPreview() {
    val sampleGames = listOf(
        GameData(1, "The Witcher 3: Wild Hunt", "", 92, 4.8f, GameData.testPlatforms(),
            GameData.testFamilies()
        ),
        GameData(2, "Red Dead Redemption 2", "", 97, 4.9f, listOf(PlatformArData.playStation(),
            PlatformArData.pc()), listOf(
            PlatformFamily.PLAYSTATION, PlatformFamily.PC)
        ),
        GameData(3, "God of War", "", 94, 4.7f, listOf(PlatformArData.playStation()),
            listOf(PlatformFamily.PLAYSTATION)
        ),
        GameData(4, "Cyberpunk 2077", "", 86, 4.1f, GameData.testPlatforms(),
            GameData.testFamilies()
        )
    )

    GameVaultTheme {
        SharedTransitionLayout {
            AnimatedVisibility(visible = true) {
                MainScreenContent(
                    uiState = UiState.Success(sampleGames),
                    onDetailClick = {},
                    onFavoriteClick = {_, _, _ -> },
                    innerPadding = PaddingValues(0.dp),
                    sharedTransitionScope = this@SharedTransitionLayout,
                    animatedVisibilityScope = this@AnimatedVisibility,
                    onRetry = {}
                )
            }
        }
    }
}