package com.kychnoo.gamevault.ui.screens

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.fromHtml
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.kychnoo.gamevault.R
import com.kychnoo.gamevault.data.model.gameDetail.GameDetailData
import com.kychnoo.gamevault.data.model.ui.UiState
import com.kychnoo.gamevault.ui.viewModel.GameDetailViewModel
import com.kychnoo.gamevault.ui.widgets.details.GameDescriptionWidget
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.selects.select
import kotlinx.serialization.Serializable
import org.koin.androidx.compose.koinViewModel
import kotlin.math.min

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
    viewModel: GameDetailViewModel = koinViewModel(),
    onBackClick: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val state = uiState

    LaunchedEffect(id) {
        viewModel.getGameDetails(id)
    }

    GameDetailScreenContent(
        id = id,
        imageUrl = imageUrl,
        gameDetailData = (state as? UiState.Success)?.data,
        sharedTransitionScope = sharedTransitionScope,
        animatedVisibilityScope = animatedVisibilityScope,
        viewModel = viewModel,
        onBackClick = onBackClick
    )
}

@Composable
private fun GameDetailScreenContent(
    id: Int,
    imageUrl: String,
    gameDetailData: GameDetailData?,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    viewModel: GameDetailViewModel,
    onBackClick: () -> Unit,
) {
    gameDetailData?.let { data ->
        Log.d("Game Detail Screen",
            data.platforms.joinToString(separator = "\n", transform = { it.platform.name })
        )
    }

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
            AsyncImage(
                model = gameDetailData?.backgroundImageAdditional,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(headerHeight + 100.dp)
                    .graphicsLayer {
                        translationY = scrollState.value * -0.4f
                    },
                contentScale = ContentScale.Crop
            )

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
                    if (gameDetailData != null) {
                        Column(
                            modifier = Modifier
                                .padding(top = 50.dp, start = 16.dp, end = 16.dp)
                                .fillMaxWidth()
                        ) {
                            Text(
                                text = gameDetailData.name,
                                style = MaterialTheme.typography.headlineLarge
                            )
                            Spacer(Modifier.height(20.dp))
                            Text(
                                text = stringResource(R.string.about_this_game),
                                style = MaterialTheme.typography.headlineMedium
                            )
                            Spacer(Modifier.height(5.dp))
                            GameDescriptionWidget(gameDetailData.description)
                        }
                    } else {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 50.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                }
            }

            if (gameDetailData != null) {
                /*  Topbar.  */
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
                            text = gameDetailData.name,
                            style = MaterialTheme.typography.titleMedium,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
        }
    }
}