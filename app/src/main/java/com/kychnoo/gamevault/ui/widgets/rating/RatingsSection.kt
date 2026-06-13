package com.kychnoo.gamevault.ui.widgets.rating

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.kychnoo.gamevault.R
import com.kychnoo.gamevault.ui.widgets.metacritic.MetacriticBadge

@Composable
fun RatingsSection(
    metacriticScore: Int?,
    rawgRating: Float?,
    ratingsCount: Int?,
    modifier: Modifier = Modifier
) {
    if (metacriticScore != null || (rawgRating != null && ratingsCount != null)) {
        Column(
            modifier = modifier
                .fillMaxWidth()
        ) {
            Text(
                text = stringResource(R.string.game_ratings),
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(start = 4.dp, end = 4.dp, bottom = 12.dp)
            )

            if (rawgRating != null && ratingsCount != null) RawgRatingBar(
                rating = rawgRating,
                ratingsCount = ratingsCount
            )
            Spacer(Modifier.height(5.dp))
            if (metacriticScore != null) MetacriticBadge(score = metacriticScore)
        }
    }
}