package com.kychnoo.gamevault.ui.screens

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.kychnoo.gamevault.data.model.gameDetail.GameDetailData
import com.kychnoo.gamevault.data.model.ui.UiState
import com.kychnoo.gamevault.ui.viewModel.GameDetailViewModel
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
    with(sharedTransitionScope) {
        Column(modifier = Modifier.fillMaxSize()) {
            Box(modifier = Modifier.fillMaxWidth().height(300.dp)) {
                AsyncImage(
                    model = gameDetailData?.backgroundImageAdditional,
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )

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
                ) {
                    AsyncImage(
                        model = imageUrl,
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }
            }

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
                }
            } else {
                CircularProgressIndicator(
                    modifier = Modifier
                        .padding(top = 50.dp)
                        .align(Alignment.CenterHorizontally)
                )
            }
        }
    }
}