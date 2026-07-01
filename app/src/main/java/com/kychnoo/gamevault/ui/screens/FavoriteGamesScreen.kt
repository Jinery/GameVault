package com.kychnoo.gamevault.ui.screens

import android.util.Log
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavBackStackEntry
import com.kychnoo.gamevault.R
import com.kychnoo.gamevault.data.model.GameData
import com.kychnoo.gamevault.data.model.ui.UiState
import com.kychnoo.gamevault.ui.viewModel.FavoritesViewModel
import com.kychnoo.gamevault.ui.widgets.loading.CircularLoader
import kotlinx.serialization.Serializable
import org.koin.androidx.compose.koinViewModel

@Serializable
object FavoritesScreenRoute

@Composable
fun FavoriteGamesScreen(
    innerPadding: PaddingValues,
    onDetailClick: (GameData) -> Unit,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    backStackEntry: NavBackStackEntry,
    modifier: Modifier = Modifier,
    viewModel: FavoritesViewModel = koinViewModel(viewModelStoreOwner = backStackEntry),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    FavoritesGamesScreenContent(
        state = uiState,
        innerPadding = innerPadding,
        onDetailClick = onDetailClick,
        onFavoriteClick = { id, gameName, isFavorite -> viewModel.launchToggleFavorite(viewModel.viewModelScope, id, gameName, isFavorite) },
        onRetry = { viewModel.loadFavorites() },
        sharedTransitionScope = sharedTransitionScope,
        animatedVisibilityScope = animatedVisibilityScope,
        modifier = modifier
    )
}

@Composable
private fun FavoritesGamesScreenContent(
    state: UiState<List<GameData>>,
    innerPadding: PaddingValues,
    onDetailClick: (GameData) -> Unit,
    onFavoriteClick: (id: Int, name: String, isFavorite: Boolean) -> Unit,
    onRetry: () -> Unit,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.fillMaxSize()) {
        when (state) {
            is UiState.Success<List<GameData>> -> {
                val games = state.data
                LaunchedEffect(games) {
                    Log.d("CheckFavoritesGames", games.joinToString(separator = "\n") { it.id.toString() })
                }
                if (games.isEmpty()) {
                    Text(
                        text = stringResource(R.string.no_favorites),
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.align(Alignment.Center)
                    )
                } else {
                    GamesGrid(
                        games = games,
                        innerPadding = innerPadding,
                        onDetailClick = onDetailClick,
                        onFavoriteClick = onFavoriteClick,
                        sharedTransitionScope = sharedTransitionScope,
                        animatedVisibilityScope = animatedVisibilityScope,
                        animateCards = true
                    )
                }
            }
            UiState.Loading -> CircularLoader(modifier = Modifier.align(Alignment.Center))
            is UiState.Error -> ErrorMessage(
                message = state.message,
                onRetry = onRetry,
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}