package com.kychnoo.gamevault.ui.screens

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.SharedTransitionScope
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.kychnoo.gamevault.R
import com.kychnoo.gamevault.data.model.gameDetail.GameDetailData
import com.kychnoo.gamevault.data.model.screenshots.ScreenshotData
import com.kychnoo.gamevault.data.model.ui.UiState
import com.kychnoo.gamevault.data.model.ui.states.GameDetailsUiState
import com.kychnoo.gamevault.ui.viewModel.GameDetailViewModel
import com.kychnoo.gamevault.ui.widgets.SharedImageOverlayContainer
import com.kychnoo.gamevault.ui.widgets.details.GameDescriptionWidget
import com.kychnoo.gamevault.ui.widgets.platform.PlatformDetailsRow
import com.kychnoo.gamevault.ui.widgets.screenshots.ScreenshotsRow
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
    onBackClick: () -> Unit,
    viewModel: GameDetailViewModel = koinViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(id) {
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
            }
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
) {
    val gameDetailState = uiState.gameDetailsState

    val scrollState = rememberScrollState()
    val density = LocalDensity.current

    val headerHeight = 300.dp
    val toolbarThresholdPx = with(density) { 300.dp.toPx() }

    val toolbarAlpha by remember {
        derivedStateOf {
            (scrollState.value / toolbarThresholdPx).coerceIn(0f, 1f)
        }
    }

    with(sharedTransitionScope) {
        Box(modifier = Modifier.fillMaxSize()) {
            /*  Background image with parallax effect.  */
            when (gameDetailState) {
                is UiState.Success -> {
                    AsyncImage(
                        model = gameDetailState.data.backgroundImageAdditional,
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(headerHeight + 100.dp)
                            .graphicsLayer {
                                translationY = scrollState.value * -0.4f
                            },
                        contentScale = ContentScale.Crop
                    )
                }
                else -> { /*  Background image not be displayed without loaded data.  */ }
            }

            /*  Back button.  */
            val backButtonAlpha = (1f - (toolbarAlpha * 2.5f)).coerceIn(0f, 1f)
            Image(
                painter = painterResource(R.drawable.ic_back),
                contentDescription = "back_button",
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onBackground),
                modifier = Modifier
                    .statusBarsPadding()
                    .padding(start = 16.dp, top = 16.dp)
                    .size(34.dp)
                    .background(Color.Black.copy(alpha = backButtonAlpha * 0.3f), shape = CircleShape)
                    .padding(4.dp)
                    .align(Alignment.TopStart)
                    .graphicsLayer {
                        alpha = backButtonAlpha
                    }
                    .clickable(
                        enabled = backButtonAlpha > 0f,
                        onClick = onBackClick
                    )
                    .zIndex(1f)
            )

            /*  Empty space for image with parallax.  */
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .align(Alignment.BottomCenter)
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                MaterialTheme.colorScheme.background
                            )
                        )
                    )
            )

            /*  Column for Content.  */
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
            ) {
                /*  Game icon.  */
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(headerHeight)
                ) {
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier
                            .padding(start = 16.dp)
                            .size(80.dp)
                            .align(Alignment.BottomStart)
                            .offset(y = 40.dp)
                            .sharedElement(
                                rememberSharedContentState(
                                    key = if (toolbarAlpha < 0.5f) "image-$id" else "disabled-main-image-$id"
                                ),
                                animatedVisibilityScope = animatedVisibilityScope,
                                clipInOverlayDuringTransition = OverlayClip(
                                    RoundedCornerShape(16.dp)
                                )
                            )
                            .zIndex(1f)
                    ) {
                        AsyncImage(
                            model = imageUrl,
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }
                }

                /*  Screen body content.  */
                Surface(
                    modifier = Modifier.fillMaxWidth().zIndex(-1f),
                    color = MaterialTheme.colorScheme.background
                ) {
                    when (gameDetailState) {
                        UiState.Loading -> {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 50.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator()
                            }
                        }
                        is UiState.Success -> {
                            val gameDetailData = gameDetailState.data
                            Column(
                                modifier = Modifier
                                    .padding(top = 50.dp)
                                    .fillMaxWidth()
                            ) {
                                Text(
                                    text = gameDetailData.name,
                                    style = MaterialTheme.typography.headlineLarge,
                                    modifier = Modifier.padding(horizontal = 16.dp)
                                )
                                Spacer(Modifier.height(20.dp))
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
                                Spacer(Modifier.height(5.dp))
                                ScreenshotsRow(
                                    screenshotsState = uiState.screenshotsState,
                                    sharedTransitionScope = sharedTransitionScope,
                                    animatedVisibilityScope = animatedVisibilityScope,
                                    onRetryClick = onRetryLoadScreenshots,
                                    onSelectImage = onSelectDetailImage,
                                )
                                Spacer(Modifier.height(5.dp))
                                PlatformDetailsRow(gameDetailData.platforms)
                                Spacer(Modifier.height(20.dp))
                            }
                        }
                        is UiState.Error -> {
                            ErrorMessage(
                                message = gameDetailState.message,
                                onRetryClick = onRetryLoad
                            )
                        }
                    }
                }
            }

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
                                    .size(38.dp)
                                    .padding(4.dp)
                                    .clickable(
                                        enabled = toolbarAlpha > 0f,
                                        onClick = onBackClick
                                    )
                            )

                            Spacer(Modifier.width(12.dp))

                            AsyncImage(
                                model = imageUrl,
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
                else -> { /*  Pass. Topbar will not be displayed without loaded data.  */ }
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