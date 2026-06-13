package com.kychnoo.gamevault.ui.widgets.games

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.kychnoo.gamevault.R
import com.kychnoo.gamevault.data.model.GameData
import com.kychnoo.gamevault.ui.widgets.GameCard

@Composable
fun SuggestedGamesLoading(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .height(64.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.loading_text),
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(5.dp))
        LinearProgressIndicator(
            color = MaterialTheme.colorScheme.primary,
            strokeCap = StrokeCap.Round,
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
        )
    }
}

@Composable
fun SuggestedGamesRow(
    rowGames: List<GameData>,
    onDetailClick: (GameData) -> Unit,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        rowGames.forEach { game ->
            GameCard(
                gameData = game,
                onCardClick = { onDetailClick(game) },
                sharedTransitionScope = sharedTransitionScope,
                animatedVisibilityScope = animatedVisibilityScope,
                modifier = Modifier.weight(1f)
            )
        }
        // When there's an odd number of games, an extra spacer maintains the column balance.
        if (rowGames.size == 1) {
            Spacer(modifier = Modifier.weight(1f))
        }
    }
}

@Composable
fun NoSuggestedGamesMessage(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(64.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = stringResource(R.string.suggested_games_not_found),
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Composable
fun SuggestedGamesErrorMessage(
    message: String,
    onRetryClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(6.dp)
            .clickable(onClick = onRetryClick),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.error_header),
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center
        )
        Text(
            text = message,
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center
        )
        Text(
            text = stringResource(R.string.click_here_for_retry),
            style = MaterialTheme.typography.bodySmall,
            textAlign = TextAlign.Center
        )
    }
}