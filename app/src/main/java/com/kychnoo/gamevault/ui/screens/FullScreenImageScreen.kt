package com.kychnoo.gamevault.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import coil3.compose.AsyncImage
import kotlinx.serialization.Serializable
import kotlin.math.abs

// Screen for display image in full-screen mode.
@Composable
fun FullScreenImageScreen(
    imageUrl: String,
    imageId: Int,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    onDismiss: () -> Unit,
) {
    // Offsets for photo positions.
    var offsetY by remember { mutableFloatStateOf(0f) }
    var offsetX by remember { mutableFloatStateOf(0f) }

    // Animations for smooth photo transitions.
    val animatedOffsetY by animateFloatAsState(
        targetValue = offsetY,
        animationSpec = spring(stiffness = Spring.StiffnessMediumLow),
        label = "drag_image_y"
    )

    val animatedOffestX by animateFloatAsState(
        targetValue = offsetX,
        animationSpec = spring(stiffness = Spring.StiffnessMediumLow),
        label = "drag_image_x"
    )

    val backgroundAlpha by remember {
        derivedStateOf {
            (1f - (abs(offsetY) / 500)).coerceIn(0f, 1f)
        }
    }

    BackHandler(true) {
        onDismiss() // Close full-screen image if user use back event.
    }

    with (sharedTransitionScope) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = backgroundAlpha))
                .pointerInput(Unit) { // Intercept the movement with our finger.
                    detectDragGestures(
                        onDrag = { change, dragAmount ->
                            change.consume()
                            // Add offsets using dragAmount.
                            offsetY += dragAmount.y
                            offsetX += dragAmount.x
                        },
                        onDragEnd = {
                            // When the event ends, check that the photo is far enough away from starting position.
                            if (abs(offsetY) > 300f) {
                                onDismiss()
                            } else {
                                offsetY = 0f
                                offsetX = 0f
                            }
                        }
                    )
                }
        ) {
            AsyncImage(
                model = imageUrl,
                contentDescription = "detail_show_image_$imageId",
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.Center)
                    .graphicsLayer {
                        // Set up photo position using animated offsets.
                        translationY = animatedOffsetY
                        translationX = animatedOffestX
                        val scale = (1f - (abs(animatedOffsetY) / 2000f)).coerceIn(0.8f, 1f)
                        scaleX = scale
                        scaleY = scale
                    }
                    .sharedBounds(
                        rememberSharedContentState(key = "screenshot-$imageId"),
                        animatedVisibilityScope = animatedVisibilityScope
                    ),
                contentScale = ContentScale.Fit
            )
        }
    }
}