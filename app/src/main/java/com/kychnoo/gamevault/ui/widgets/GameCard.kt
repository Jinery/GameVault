package com.kychnoo.gamevault.ui.widgets

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.kychnoo.gamevault.R
import com.kychnoo.gamevault.data.model.GameData
import com.kychnoo.gamevault.ui.theme.GameVaultTheme
import com.kychnoo.gamevault.ui.theme.cardColor
import com.kychnoo.gamevault.ui.widgets.favorites.BorderedFavoriteButton
import com.kychnoo.gamevault.ui.widgets.platform.PlatformsRow

@Composable
fun GameCard(
    gameData: GameData,
    onCardClick: () -> Unit,
    onFavoriteClick: () -> Unit,
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
            interactionSource = remember { MutableInteractionSource() },
            indication = null,
            onClick = onCardClick
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.cardColor
        ),
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(modifier = Modifier.fillMaxSize()) {
                with(sharedTransitionScope) {
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
                        PlatformsRow(gameData.platformFamilies)
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

            BorderedFavoriteButton(
                isFavorite = gameData.isFavorite,
                onClick = onFavoriteClick,
                modifier = Modifier.align(Alignment.TopEnd).padding(12.dp)
            )
        }
    }
}

@Preview
@Composable
private fun GameCardPreview() {
    Box(Modifier.height(300.dp).width(200.dp)) {
        val sampleGame = GameData(1, "The Witcher 3: Wild Hunt", "", 92, 4.8f, GameData.testPlatforms(),
            GameData.testFamilies()
        )
        GameVaultTheme {
            SharedTransitionLayout {
                AnimatedVisibility(visible = true) {
                    GameCard(
                        gameData = sampleGame,
                        onCardClick = { },
                        onFavoriteClick = {  },
                        sharedTransitionScope = this@SharedTransitionLayout,
                        animatedVisibilityScope = this@AnimatedVisibility,
                    )
                }
            }
        }
    }
}