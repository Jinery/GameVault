package com.kychnoo.gamevault.ui.widgets.metacritic

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kychnoo.gamevault.R
import com.kychnoo.gamevault.ui.theme.MetacriticGreen
import com.kychnoo.gamevault.ui.theme.MetacriticRed
import com.kychnoo.gamevault.ui.theme.MetacriticYellow

@Composable
fun MetacriticBadge(score: Int, modifier: Modifier = Modifier) {
    val cardColor = when {
        score >= 81 -> MetacriticGreen
        score >= 61 -> MetacriticYellow
        else -> MetacriticRed
    }
    val textColor = if (score in 61..80) Color.Black else Color.White

    Card(
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = cardColor, contentColor = textColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        modifier = modifier.fillMaxWidth().heightIn(80.dp, 128.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(3.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Image(
                painter = painterResource(R.drawable.ic_metacritic),
                contentDescription = "metacritic_badge_icon",
                modifier = Modifier.size(96.dp)
            )
            Spacer(Modifier.width(5.dp))
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceEvenly,
            ) {
                Text(
                    text = "Metacritic",
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.5.sp
                )
                Text(
                    text = "$score/100",
                    fontSize = 36.sp,
                    fontWeight = FontWeight.ExtraBold
                )
            }
        }
    }
}

@Preview
@Composable
fun MetacriticBadgePreview() {
    Box(
        modifier = Modifier
            .width(300.dp)
            .height(200.dp),
        contentAlignment = Alignment.Center
    ) {
        MetacriticBadge(81)
    }
}