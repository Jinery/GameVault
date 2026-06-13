package com.kychnoo.gamevault.ui.widgets

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.snap
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.kychnoo.gamevault.R
import com.kychnoo.gamevault.data.model.GameData
import com.kychnoo.gamevault.ui.theme.cardColor
import com.kychnoo.gamevault.ui.widgets.platform.PlatformsRow
import kotlinx.coroutines.delay

@Composable
fun GameCard(
    gameData: GameData,
    onCardClick: (Int) -> Unit,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    modifier: Modifier = Modifier,
) {

    Card(
        shape = RoundedCornerShape(24.dp),
        modifier = modifier
        .fillMaxWidth()
        .aspectRatio(0.7f)
        .clickable(
            onClick = { onCardClick(gameData.id) }
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.cardColor
        ),
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            with (sharedTransitionScope) {
                AsyncImage(
                    model = gameData.imageUrl,
                    placeholder = painterResource(R.drawable.game_card_placeholder),
                    contentDescription = gameData.title,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1.5f)
                        .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
                        .sharedElement(
                            sharedContentState = rememberSharedContentState(key = "image-${gameData.id}"),
                            animatedVisibilityScope = animatedVisibilityScope,
                            clipInOverlayDuringTransition = OverlayClip(
                                RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
                            )
                        ),
                    contentScale = ContentScale.Crop
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(12.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    PlatformsRow(gameData.platforms)
                    if (gameData.score != null) RatingBadge(gameData.score)
                }

                Text(
                    text = gameData.title,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.ExtraBold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}