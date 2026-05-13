package com.kychnoo.gamevault.ui.widgets

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kychnoo.gamevault.ui.theme.ratingGreen
import com.kychnoo.gamevault.ui.theme.ratingRed
import com.kychnoo.gamevault.ui.theme.ratingYellow

@Composable
fun RatingBadge(score: Int, modifier: Modifier = Modifier) {
    val backgroundColor = when {
        score >= 80 -> MaterialTheme.colorScheme.ratingGreen
        score >= 50 -> MaterialTheme.colorScheme.ratingYellow
        else -> MaterialTheme.colorScheme.ratingRed
    }

    Surface(
        color = backgroundColor.copy(alpha = 0.1f),
        shape = RoundedCornerShape(4.dp),
        border = BorderStroke(0.4.dp, backgroundColor),
        modifier = modifier
    ) {
        Text(
            text = score.toString(),
            color = backgroundColor,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            modifier = Modifier.padding(
                horizontal = 5.dp,
                vertical = 1.dp
            )
        )
    }
}

@Preview
@Composable
fun RatingBadgePreview() {
    RatingBadge(92)
}