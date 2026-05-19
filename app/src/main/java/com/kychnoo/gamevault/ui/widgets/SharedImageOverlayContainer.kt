package com.kychnoo.gamevault.ui.widgets

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.kychnoo.gamevault.ui.screens.FullScreenImageScreen

private data class OverlayImageData(val id: Int, val url: String)

// Overlay for displaying photos in full-screen mode.
@Composable
fun SharedImageOverlayContainer(
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    modifier: Modifier = Modifier,
    content: @Composable (onImageClick: (id: Int, url: String) -> Unit) -> Unit,
) {
    // Active image for full-screen dislay.
    var activeImage by rememberSaveable { mutableStateOf<OverlayImageData?>(null) }

    content { imageId, imageUrl ->
        activeImage = OverlayImageData(imageId, imageUrl) // Setting the image in call.
    }

    // For animated photo displays
    AnimatedContent(
        targetState = activeImage,
        transitionSpec = {
            fadeIn().togetherWith(fadeOut())
        },
            label = "image_overlay_transition",
            modifier = modifier.fillMaxSize()
        ) { targetActiveImage ->
            targetActiveImage?.let { // If targetActiveImage is not null, show image in full-screen mode.
                FullScreenImageScreen(
                    imageUrl = targetActiveImage.url,
                    imageId = targetActiveImage.id,
                    sharedTransitionScope = sharedTransitionScope,
                    animatedVisibilityScope = animatedVisibilityScope,
                    onDismiss = { activeImage = null }
                )
            }
    }
}