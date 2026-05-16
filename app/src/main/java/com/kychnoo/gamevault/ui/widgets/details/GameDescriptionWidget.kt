package com.kychnoo.gamevault.ui.widgets.details

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.fromHtml
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.kychnoo.gamevault.R

@Composable
fun GameDescriptionWidget(descriptionText: String, modifier: Modifier = Modifier) {
    // Boolean flag to determine if the card is expanded.
    var isShow by rememberSaveable { mutableStateOf(false) }

    // Animate float as state for arrow rotation animation.
    val arrowRotation by animateFloatAsState(
        targetValue = if (isShow)  180f else 0f,
        animationSpec = spring(stiffness = Spring.StiffnessLow),
        label = "ArrowRotation"
    )

    Card(
        modifier = modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(2.dp),
        border = BorderStroke(width = 1.dp, color = MaterialTheme.colorScheme.onBackground)
    ) {
        // Container with gradient for description text.
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(6.dp)
                .animateContentSize(animationSpec = spring(stiffness = Spring.StiffnessMediumLow))
                .then(
                    if (!isShow) { // Draw gradient if the card is not expanded.
                        Modifier.drawWithContent {
                            drawContent()

                            drawRect(
                                brush = Brush.verticalGradient(
                                    colors = listOf(Color.Transparent, Color.Gray),
                                    // 2% of box for text height.
                                    startY = size.height * 0.2f,
                                    endY = size.height
                                ),
                                blendMode = BlendMode.DstOut // Cut alpha around gradient.
                            )
                        }
                    }
                    else Modifier
                )
        ) {
            Text(
                text = AnnotatedString.fromHtml(descriptionText),
                style = MaterialTheme.typography.bodyMedium,
                maxLines = if (isShow) Int.MAX_VALUE else 4,
                overflow = TextOverflow.Clip
            )
        }

        // Down panel.
        Row(
            modifier = Modifier.fillMaxWidth().padding(start = 12.dp).padding(2.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Image(
                painter = painterResource(R.drawable.ic_down),
                contentDescription = "show-description-button",
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onBackground),
                modifier = Modifier
                    .size(24.dp)
                    .graphicsLayer {
                        rotationZ = arrowRotation
                    }
                    .clickable(
                        onClick = {
                            isShow = !isShow
                        }
                    )
            )

            Spacer(Modifier.width(8.dp))

            Text(
                text = stringResource(
                    if (isShow) R.string.hide_description else R.string.show_description,
                ),
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold
            )
        }
    }
}