package com.kychnoo.gamevault.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButtonDefaults.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavBackStackEntry
import com.kychnoo.gamevault.R
import com.kychnoo.gamevault.data.model.GameData
import com.kychnoo.gamevault.data.model.ui.UiState
import com.kychnoo.gamevault.ui.theme.cardColor
import com.kychnoo.gamevault.ui.viewModel.SearchViewModel
import com.kychnoo.gamevault.ui.widgets.bottom.GamesFilterBottomSheet
import com.kychnoo.gamevault.ui.widgets.loading.CircularLoader
import com.kychnoo.gamevault.ui.widgets.search.bar.SearchBar
import com.kychnoo.gamevault.ui.widgets.search.history.HistoryItem
import kotlinx.serialization.Serializable
import org.koin.androidx.compose.koinViewModel

@Serializable object SearchScreenRoute

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    onDetailClick: (GameData) -> Unit,
    innerPadding: PaddingValues,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    backStackEntry: NavBackStackEntry,
    modifier: Modifier = Modifier,
    searchViewModel: SearchViewModel = koinViewModel(viewModelStoreOwner = backStackEntry)
) {
    val uiState by searchViewModel.uiState.collectAsStateWithLifecycle()

    var searchBarExpanded by rememberSaveable { mutableStateOf(false) }
    var showFilterSheet by remember { mutableStateOf(false) }

    Box(modifier = modifier.fillMaxSize()) {
        Row(modifier = Modifier
            .align(Alignment.TopCenter)
            .fillMaxWidth()
            .padding(PaddingValues(
                top = innerPadding.calculateTopPadding() + 4.dp,
                start = 8.dp,
                end = 8.dp
            ))
            .zIndex(1f),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            SearchBar(
                modifier = Modifier.weight(1f),
                query = uiState.searchQuery,
                onQueryChange = searchViewModel::updateSearchQuery,
                expanded = searchBarExpanded,
                onExpandedChange = { searchBarExpanded = it },
                onSearch = { searchViewModel.performSearch(uiState.searchQuery) },
            ) {
                if (uiState.searchHistory.isNotEmpty()) {
                    LazyColumn {
                        items(
                            items = uiState.searchHistory,
                            key = { entry -> entry.id }
                        ) { entry ->
                            HistoryItem(
                                query = entry.query,
                                onDelete = { searchViewModel.deleteHistoryEntry(entry) },
                                onClick = { searchViewModel.updateSearchQuery(entry.query) },
                                modifier = Modifier.animateItem(
                                    fadeInSpec = tween(300),
                                    placementSpec = spring(
                                        dampingRatio = Spring.DampingRatioMediumBouncy,
                                        stiffness = Spring.StiffnessLow
                                    )
                                )
                            )

                            if (uiState.searchHistory.lastOrNull() != entry) {
                                HorizontalDivider(
                                    modifier = Modifier.padding(horizontal = 16.dp),
                                    thickness = 0.5.dp,
                                    color = MaterialTheme.colorScheme.outlineVariant
                                )
                            }
                        }
                    }
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(140.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = stringResource(R.string.no_search_history),
                            style = MaterialTheme.typography.titleSmall,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            AnimatedVisibility(
                visible = !searchBarExpanded,
                enter = slideInHorizontally(tween(300), initialOffsetX = { it }) +
                        expandHorizontally(tween(300), expandFrom = Alignment.End) +
                        fadeIn(tween(200)),
                exit = slideOutHorizontally(tween(300), targetOffsetX = { it }) +
                        shrinkHorizontally(tween(300), shrinkTowards = Alignment.End) +
                        fadeOut(tween(200))
            ) {
                IconButton(
                    onClick = { showFilterSheet = true },
                    modifier = Modifier
                        .size(56.dp)
                        .background(
                            color = MaterialTheme.colorScheme.surfaceContainerHigh,
                            shape = CircleShape
                        )
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_filter),
                        contentDescription = "game_filters_button",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }

        SearchScreenContent(
            state = uiState.games,
            innerPadding = innerPadding,
            onDetailClick = onDetailClick,
            onRetry = { searchViewModel.performSearch(uiState.searchQuery) },
            sharedTransitionScope = sharedTransitionScope,
            animatedVisibilityScope = animatedVisibilityScope,
            modifier = modifier
        )

        if (showFilterSheet) {
            GamesFilterBottomSheet(
                initialFilters = uiState.filters,
                genresState = uiState.availableGenres,
                onDismissRequest = { showFilterSheet = false },
                onApplyFilters = { filters ->
                    searchViewModel.applyFilters(filters)
                    showFilterSheet = false
                },
            )
        }
    }
}

@Composable
private fun BoxScope.SearchScreenContent(
    state: UiState<List<GameData>>,
    innerPadding: PaddingValues,
    onDetailClick: (GameData) -> Unit,
    onRetry: () -> Unit,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    modifier: Modifier = Modifier
) {
    var firstStart by rememberSaveable { mutableStateOf(true) }

    LaunchedEffect(state) {
        if (state is UiState.Success<List<GameData>> && state.data.isNotEmpty()) {
            firstStart = false
        }
    }

    when (state) {
        UiState.Loading -> CircularLoader(modifier = Modifier.align(Alignment.Center))
        is UiState.Success<List<GameData>> -> {
            if (state.data.isEmpty()) {
                if (firstStart) {
                    Text(
                        text = stringResource(R.string.look_something),
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.align(Alignment.Center)
                    )
                } else {
                    Text(
                        text = stringResource(R.string.search_games_no_found),
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            } else {
                GamesGrid(
                    games = state.data,
                    innerPadding = PaddingValues(
                        top = innerPadding.calculateTopPadding() + 64.dp,
                        bottom = innerPadding.calculateBottomPadding(),
                    ),
                    onDetailClick = onDetailClick,
                    sharedTransitionScope = sharedTransitionScope,
                    animatedVisibilityScope = animatedVisibilityScope,
                    modifier = modifier
                )
            }
        }
        is UiState.Error -> ErrorMessage(
            message = state.message,
            onRetry = onRetry,
            modifier = modifier.align(Alignment.Center)
        )
    }
}