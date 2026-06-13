package com.kychnoo.gamevault.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavBackStackEntry
import coil3.compose.AsyncImage
import com.kychnoo.gamevault.R
import com.kychnoo.gamevault.data.formatter.format
import com.kychnoo.gamevault.data.model.GameData
import com.kychnoo.gamevault.data.model.ui.UiState
import com.kychnoo.gamevault.data.model.ui.states.GameDetailsUiState
import com.kychnoo.gamevault.ui.viewModel.GameDetailViewModel
import com.kychnoo.gamevault.ui.widgets.SharedImageOverlayContainer
import com.kychnoo.gamevault.ui.widgets.details.GameDescriptionWidget
import com.kychnoo.gamevault.ui.widgets.development.GameDevelopmentTeamCard
import com.kychnoo.gamevault.ui.widgets.games.NoSuggestedGamesMessage
import com.kychnoo.gamevault.ui.widgets.games.SuggestedGamesErrorMessage
import com.kychnoo.gamevault.ui.widgets.games.SuggestedGamesLoading
import com.kychnoo.gamevault.ui.widgets.games.SuggestedGamesRow
import com.kychnoo.gamevault.ui.widgets.platform.PlatformDetailsRow
import com.kychnoo.gamevault.ui.widgets.rating.RatingsSection
import com.kychnoo.gamevault.ui.widgets.screenshots.ScreenshotsRow
import com.kychnoo.gamevault.ui.widgets.website.WebsiteButton
import kotlinx.serialization.Serializable
import org.koin.androidx.compose.koinViewModel

@Serializable data class GameDetail(
    val id: Int,
    val imageUrl: String = ""
)

@Composable
fun GameDetailScreen(
    id: Int,
    imageUrl: String,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    backStackEntry: NavBackStackEntry,
    onBackClick: () -> Unit,
    onGameDetailClick: (GameData) -> Unit,
    viewModel: GameDetailViewModel = koinViewModel(viewModelStoreOwner = backStackEntry),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(viewModel) {
        viewModel.loadGameData(id)
    }

    SharedImageOverlayContainer(
        sharedTransitionScope = sharedTransitionScope,
        animatedVisibilityScope = animatedVisibilityScope,
    ) { openImage -> // This function called on open image.
        GameDetailScreenContent(
            id = id,
            imageUrl = imageUrl,
            uiState = uiState,
            sharedTransitionScope = sharedTransitionScope,
            animatedVisibilityScope = animatedVisibilityScope,
            onSelectDetailImage = { imageId, imageUrl ->
                openImage(imageId, imageUrl) // Open selected image.
            },
            onBackClick = onBackClick,
            onRetryLoad = {
                viewModel.getGameDetails(id)
            },
            onRetryLoadScreenshots = {
                viewModel.getGameScreenshots(id)
            },
            onRetryLoadDevelopmentTeamsInfo = {
                viewModel.getDevelopmentTeamsForGame(id)
            },
            onSuggestedGamesRetryClick = {
                viewModel.getSuggestedGames(id)
            },
            onGameDetailClick = onGameDetailClick,
        )
    }
}

@Composable
private fun GameDetailScreenContent(
    id: Int,
    imageUrl: String,
    uiState: GameDetailsUiState,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    onSelectDetailImage: (Int, String) -> Unit,
    onBackClick: () -> Unit,
    onRetryLoad: () -> Unit,
    onRetryLoadScreenshots: () -> Unit,
    onSuggestedGamesRetryClick: () -> Unit,
    onGameDetailClick: (GameData) -> Unit,
    onRetryLoadDevelopmentTeamsInfo: () -> Unit,
) {
    val gameDetailState = uiState.gameDetailsState

    val lazyListState = rememberLazyListState()
    val density = LocalDensity.current

    // Toolbar fades in as the user scrolls past the hero image (300dp threshold).
    val toolbarAlpha by remember {
        derivedStateOf {
            if (lazyListState.firstVisibleItemIndex > 0) {
                1f
            } else {
                val toolbarThresholdPx = with(density) { 300.dp.toPx() }
                (lazyListState.firstVisibleItemScrollOffset / toolbarThresholdPx).coerceIn(0f, 1f)
            }
        }
    }

    // Using rememberSaveable ensures contentVisible survives configuration changes
    // and recomposition when the screen returns from the back stack.
    var contentVisible by rememberSaveable { mutableStateOf(false) }
    LaunchedEffect(id, gameDetailState) {
        if (gameDetailState is UiState.Success) {
            if (!contentVisible) {
                // Trigger animations only when game details are fully loaded.
                contentVisible = true
            }
        }
    }

    with(sharedTransitionScope) {
        Box(modifier = Modifier.fillMaxSize()) {

            /*  Screen body content.  */
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth(),
                state = lazyListState
            ) {
                item(key = "background") {
                    when (gameDetailState) {
                        is UiState.Success -> {
                            AsyncImage(
                                model = gameDetailState.data.backgroundImageAdditional,
                                contentDescription = "background_image",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(300.dp),
                                contentScale = ContentScale.Crop
                            )
                        }
                        else -> Box(modifier = Modifier.height(300.dp))
                    }
                }
                item(key = "game_icon") {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 16.dp)
                    ) {
                        Card(
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier
                                .size(80.dp)
                                .align(Alignment.BottomStart)
                                .offset(y = (-40).dp)
                                .sharedElement(
                                    rememberSharedContentState(
                                        // Dynamically change key to disable shared transition when toolbar is visible,
                                        // avoiding conflict with the toolbar's own shared element.
                                        key = if (toolbarAlpha < 0.5f) "image-$id" else "disabled-main-image-$id"
                                    ),
                                    animatedVisibilityScope = animatedVisibilityScope,
                                    clipInOverlayDuringTransition = OverlayClip(
                                        RoundedCornerShape(16.dp)
                                    )
                                )
                        ) {
                            AsyncImage(
                                model = imageUrl,
                                placeholder = painterResource(R.drawable.game_card_placeholder),
                                contentDescription = null,
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        }
                    }
                }
                when (gameDetailState) {
                    is UiState.Error -> {
                        item {
                            ErrorMessage(
                                message = gameDetailState.message,
                                onRetryClick = onRetryLoad,
                                modifier = Modifier.padding(top = 100.dp)
                            )
                        }
                    }

                    UiState.Loading -> item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(400.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }

                    is UiState.Success -> {
                        val gameDetailData = gameDetailState.data
                        // Different delay values create a cascading entrance animation.
                        item(key = "game_name") {
                            AnimatedVisibility(
                                visible = contentVisible,
                                enter = fadeIn(tween(300)) + slideInVertically(
                                    initialOffsetY = { it / 4 },
                                    animationSpec = tween(300, delayMillis = 100)
                                )
                            ) {
                                Text(
                                    text = gameDetailData.name,
                                    style = MaterialTheme.typography.headlineLarge,
                                    modifier = Modifier.padding(horizontal = 16.dp)
                                )
                            }
                        }
                        item(key = "spacer_1") {
                            Spacer(Modifier.height(20.dp))
                        }
                        item(key = "game_description") {
                            AnimatedVisibility(
                                visible = contentVisible,
                                enter = fadeIn(tween(300, delayMillis = 200)) +
                                        slideInVertically(
                                            initialOffsetY = { it / 4 },
                                            animationSpec = tween(300, delayMillis = 200)
                                        )
                            ) {
                                Column(modifier = Modifier.animateContentSize()) {
                                    Text(
                                        text = stringResource(R.string.about_this_game),
                                        style = MaterialTheme.typography.headlineMedium,
                                        modifier = Modifier.padding(horizontal = 16.dp)
                                    )
                                    Spacer(Modifier.height(5.dp))
                                    GameDescriptionWidget(
                                        descriptionText = gameDetailData.description,
                                        modifier = Modifier.padding(horizontal = 16.dp)
                                    )
                                }
                            }
                        }
                        item(key = "spacer_2") {
                            Spacer(Modifier.height(5.dp))
                        }
                        item(key = "game_screenshots_row") {
                            AnimatedVisibility(
                                visible = contentVisible,
                                enter = fadeIn(tween(300, delayMillis = 300))
                            ) {
                                ScreenshotsRow(
                                    screenshotsState = uiState.screenshotsState,
                                    sharedTransitionScope = sharedTransitionScope,
                                    animatedVisibilityScope = animatedVisibilityScope,
                                    onRetryClick = onRetryLoadScreenshots,
                                    onSelectImage = onSelectDetailImage,
                                )
                            }
                        }
                        item(key = "spacer_3") {
                            Spacer(Modifier.height(5.dp))
                        }
                        item(key = "game_platform_details_row") {
                            AnimatedVisibility(
                                visible = contentVisible,
                            ) {
                                if (gameDetailData.platforms.isNotEmpty()) {
                                    PlatformDetailsRow(gameDetailData.platforms)
                                }
                            }
                        }
                        item(key = "spacer_4") {
                            Spacer(Modifier.height(5.dp))
                        }
                        item(key = "game_ratings_section") {
                            AnimatedVisibility(visible = contentVisible) {
                                RatingsSection(
                                    metacriticScore = gameDetailData.metacritic,
                                    rawgRating = gameDetailData.rating,
                                    ratingsCount = gameDetailData.ratingsCount,
                                    modifier = Modifier.padding(horizontal = 12.dp)
                                )
                            }
                        }
                        item(key = "spacer_5") {
                            Spacer(Modifier.height(5.dp))
                        }
                        item(key = "game_development_team_card") {
                            AnimatedVisibility(visible = contentVisible) {
                                GameDevelopmentTeamCard(
                                    state = uiState.developmentTeamsState,
                                    onRetryClick = onRetryLoadDevelopmentTeamsInfo,
                                    modifier = Modifier.padding(horizontal = 12.dp)
                                )
                            }
                        }
                        item(key = "spacer_6") {
                            Spacer(Modifier.height(5.dp))
                        }
                        item(key = "game_create_update_texts") {
                            AnimatedVisibility(visible = contentVisible) {
                                Column(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    Text(
                                        modifier = Modifier.fillMaxWidth(),
                                        text = stringResource(
                                            R.string.released,
                                            gameDetailData.released?.format()
                                                ?: stringResource(R.string.date_not_available)
                                        ),
                                        style = MaterialTheme.typography.titleLarge,
                                        fontWeight = FontWeight.Bold,
                                        textAlign = TextAlign.Center
                                    )
                                    Text(
                                        modifier = Modifier.fillMaxWidth(),
                                        text = stringResource(
                                            R.string.updated,
                                            gameDetailData.updated?.format()
                                                ?: stringResource(R.string.date_not_available)
                                        ),
                                        style = MaterialTheme.typography.titleLarge,
                                        fontWeight = FontWeight.Bold,
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }
                        }
                        item(key = "spacer_7") {
                            Spacer(Modifier.height(5.dp))
                        }
                        item(key = "game_website_button") {
                            AnimatedVisibility(visible = contentVisible) {
                                if (gameDetailData.website != null) {
                                    WebsiteButton(
                                        websiteUrl = gameDetailData.website,
                                        modifier = Modifier.padding(horizontal = 16.dp)
                                    )
                                }
                            }
                        }
                        item(key = "spacer_8") {
                            Spacer(Modifier.height(5.dp))
                        }
                        when (uiState.suggestedGames) {
                            UiState.Loading -> item(key = "suggested_games_loading") {
                                AnimatedVisibility(visible = contentVisible) {
                                    SuggestedGamesLoading(modifier = Modifier.padding(horizontal = 16.dp))
                                }
                            }
                            is UiState.Success -> {
                                val games = uiState.suggestedGames.data
                                if (games.isEmpty()) {
                                    item(key = "no_suggested_games_message") {
                                        AnimatedVisibility(visible = contentVisible) {
                                            NoSuggestedGamesMessage(
                                                modifier = Modifier.padding(
                                                    horizontal = 16.dp
                                                )
                                            )
                                        }
                                    }
                                } else {
                                    // Chunk into rows of 2 for a grid-like layout without nesting scrollable components.
                                    val rows = games.chunked(2)
                                    items(
                                        items = rows,
                                        key = { index -> "suggested_games_row_$index" }
                                    ) { rowGames ->
                                        AnimatedVisibility(visible = contentVisible) {
                                            SuggestedGamesRow(
                                                rowGames = rowGames,
                                                onDetailClick = onGameDetailClick,
                                                sharedTransitionScope = sharedTransitionScope,
                                                animatedVisibilityScope = animatedVisibilityScope,
                                                modifier = Modifier
                                                    .padding(horizontal = 16.dp, vertical = 5.dp)
                                            )
                                        }
                                    }
                                }
                            }
                            is UiState.Error -> item(key = "suggested_games_error_message") {
                                AnimatedVisibility(visible = contentVisible) {
                                    SuggestedGamesErrorMessage(
                                        message = uiState.suggestedGames.message,
                                        onRetryClick = onSuggestedGamesRetryClick,
                                        modifier = Modifier.padding(horizontal = 16.dp)
                                    )
                                }
                            }
                        }
                        item(key = "spacer_9") {
                            Spacer(Modifier.height(20.dp))
                        }
                    }
                }
            }

            /*  Back button.  */
            val backButtonAlpha = (1f - (toolbarAlpha * 2.5f)).coerceIn(0f, 1f)
            Image(
                painter = painterResource(R.drawable.ic_back),
                contentDescription = "back_button",
                colorFilter = ColorFilter.tint(Color.White),
                modifier = Modifier
                    .statusBarsPadding()
                    .padding(start = 16.dp, top = 16.dp)
                    .size(34.dp)
                    .background(
                        Color.Black.copy(alpha = backButtonAlpha * 0.3f),
                        shape = CircleShape
                    )
                    .padding(4.dp)
                    .align(Alignment.TopStart)
                    .graphicsLayer {
                        alpha = backButtonAlpha
                    }
                    .clickable(
                        enabled = backButtonAlpha > 0f,
                        onClick = onBackClick
                    )
            )

            when (gameDetailState) {
                /*  Topbar.  */
                is UiState.Success -> {
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(86.dp)
                            .graphicsLayer {
                                alpha = toolbarAlpha
                                translationY = (1f - toolbarAlpha) * -100f
                            }
                            .align(Alignment.TopCenter),
                        shape = RoundedCornerShape(bottomStart = 12.dp, bottomEnd = 12.dp),
                        color = MaterialTheme.colorScheme.surface,
                        shadowElevation = 4.dp
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(top = 16.dp)
                                .padding(horizontal = 16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Image(
                                painter = painterResource(R.drawable.ic_back),
                                contentDescription = "back_button",
                                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onBackground),
                                modifier = Modifier
                                    .size(32.dp)
                                    .padding(4.dp)
                                    .clickable(
                                        enabled = toolbarAlpha > 0f,
                                        onClick = onBackClick
                                    )
                            )

                            Spacer(Modifier.width(12.dp))

                            AsyncImage(
                                model = imageUrl,
                                placeholder = painterResource(R.drawable.game_card_placeholder),
                                contentDescription = null,
                                modifier = Modifier
                                    .size(44.dp)
                                    .clip(RoundedCornerShape(4.dp))
                                    .sharedElement(
                                        rememberSharedContentState(
                                            key = if (toolbarAlpha >= 0.5f) "image-$id" else "disabled-toolbar-image-$id"
                                        ),
                                        animatedVisibilityScope = animatedVisibilityScope,
                                        clipInOverlayDuringTransition = OverlayClip(
                                            RoundedCornerShape(4.dp)
                                        )
                                    ),
                                contentScale = ContentScale.Crop
                            )

                            Spacer(Modifier.width(12.dp))

                            Text(
                                text = gameDetailState.data.name,
                                style = MaterialTheme.typography.titleMedium,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }

                else -> { /*  Pass. Topbar will not be displayed without loaded data.  */
                }
            }
        }
    }
}

@Composable
private fun ErrorMessage(
    message: String,
    onRetryClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
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
        Button(onClick = onRetryClick) {
            Text(stringResource(R.string.retry))
        }
    }
}