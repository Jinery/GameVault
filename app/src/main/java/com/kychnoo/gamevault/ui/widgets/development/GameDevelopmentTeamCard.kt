package com.kychnoo.gamevault.ui.widgets.development

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import coil3.compose.AsyncImage
import com.kychnoo.gamevault.R
import com.kychnoo.gamevault.data.model.development.DevelopmentTeamPageData
import com.kychnoo.gamevault.data.model.ui.UiState

@Composable
fun GameDevelopmentTeamCard(
    state: UiState<DevelopmentTeamPageData>,
    onRetryClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = stringResource(R.string.developers),
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(4.dp)
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 3.dp
            )
        ) {
            when (state) {
                UiState.Loading -> {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp)
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

                is UiState.Success<DevelopmentTeamPageData> -> {
                    val data = state.data
                    if (data.count > 1) {
                        // Multiple developers/teams - show overlapping avatars.
                        MultiplyDevelopmentTeamCard(data)
                    } else if (data.count == 1 && data.results.first().image != null) {
                        // Single developer - show large avatar with name and game count.
                        SingleDevelopmentTeamCard(data)
                    } else {
                        Box(
                            modifier = Modifier.fillMaxWidth().size(80.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = stringResource(R.string.development_team_not_found),
                                style = MaterialTheme.typography.titleMedium
                            )
                        }
                    }
                }

                is UiState.Error -> ErrorColumn(
                    message = state.message,
                    onRetryClick = onRetryClick
                )
            }
        }
    }
}

@Composable
private fun SingleDevelopmentTeamCard(
    data: DevelopmentTeamPageData,
    modifier: Modifier = Modifier
) {
    val devTeam = data.results.first()
    Row(
        modifier = modifier.padding(6.dp)
    ) {
        AsyncImage(
            model = devTeam.image,
            contentDescription = "dev_team_${devTeam.id}_icon",
            modifier = Modifier.size(64.dp).clip(CircleShape)
        )
        Column(
            modifier = Modifier.fillMaxWidth().padding(5.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = devTeam.name,
                fontSize = 24.sp,
                fontWeight = FontWeight.ExtraBold
            )
            // Uses plural resource to show e.g. "2 games" correctly.
            Text(
                text = pluralStringResource(
                    R.plurals.dev_team_games_count,
                    devTeam.gamesCount,
                    devTeam.gamesCount
                ),
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
private fun MultiplyDevelopmentTeamCard(
    data: DevelopmentTeamPageData,
    modifier: Modifier = Modifier
) {
    // API may return entries without an image, filter them out to avoid broken UI.
    val filteredData = data.results.filter { it.image != null }
    Column(
        modifier = modifier.padding(6.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxWidth().height(64.dp),
        ) {
            filteredData.forEachIndexed { index, devTeam ->
                AsyncImage(
                    model = devTeam.image,
                    contentDescription = "dev_team_${devTeam.id}_icon",
                    modifier = Modifier
                        .size(64.dp)
                        .offset(x = (index * 32).dp) // Shift each avatar to create overlapping effect.
                        .zIndex(index.toFloat())
                        .clip(CircleShape)
                )
            }
        }

        Spacer(Modifier.height(4.dp))

        // Show all names in a comma‑separated list below the avatars.
        val allDevTeamsNames = filteredData.joinToString(separator = ",") { it.name }
        Text(
            text = allDevTeamsNames,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
private fun ErrorColumn(
    message: String,
    onRetryClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(6.dp)
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = onRetryClick), // Whole area is tappable for retry.
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
            text = stringResource(R.string.click_card_for_retry),
            style = MaterialTheme.typography.bodySmall,
            textAlign = TextAlign.Center
        )
    }
}

@Preview
@Composable
fun GameDevelopmentTeamCardPreview() {
    Box(modifier = Modifier.width(300.dp).height(200.dp), contentAlignment = Alignment.Center) {
        GameDevelopmentTeamCard(
            state = UiState.Success(DevelopmentTeamPageData.PreviewDevTeamPageData),
            onRetryClick = {  },
            modifier = Modifier.padding(12.dp)
        )
    }
}