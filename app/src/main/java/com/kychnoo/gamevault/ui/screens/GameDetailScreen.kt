package com.kychnoo.gamevault.ui.screens

import android.util.Log
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.fromHtml
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.kychnoo.gamevault.R
import com.kychnoo.gamevault.data.model.gameDetail.GameDetailData
import com.kychnoo.gamevault.data.model.ui.UiState
import com.kychnoo.gamevault.ui.viewModel.GameDetailViewModel
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
    viewModel: GameDetailViewModel = koinViewModel()
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
        viewModel = viewModel
    )
}

@Composable
private fun GameDetailScreenContent(
    id: Int,
    imageUrl: String,
    gameDetailData: GameDetailData?,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    viewModel: GameDetailViewModel
) {
    gameDetailData?.let { data ->
        Log.d("Game Detail Screen",
            data.platforms.joinToString(separator = "\n", transform = { it.platform.name })
        )
    }

    val scrollState = rememberScrollState()

    val headerHeight = 300.dp

    with(sharedTransitionScope) {
        Box(modifier = Modifier.fillMaxSize()) {
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

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
            ) {
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
                                rememberSharedContentState(key = "image-$id"),
                                animatedVisibilityScope = animatedVisibilityScope
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
                            Text(
                                AnnotatedString.fromHtml(
                                    htmlString = gameDetailData.description
                                ),
                                style = MaterialTheme.typography.bodyMedium
                            )
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
        }
    }
}