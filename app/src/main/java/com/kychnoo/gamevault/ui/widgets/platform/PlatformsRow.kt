package com.kychnoo.gamevault.ui.widgets.platform

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.kychnoo.gamevault.R
import com.kychnoo.gamevault.data.model.platform.PlatformArData
import com.kychnoo.gamevault.data.model.platform.Platform
import com.kychnoo.gamevault.data.model.platform.PlatformFamily

@Composable
fun PlatformsRow(
    platforms: List<PlatformArData>,
    modifier: Modifier = Modifier
) {
    val rawPlatforms = platforms.map { it.platform }.sortedBy { it.name }

    Row(
        modifier = modifier.padding(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        rawPlatforms.mapNotNull { Platform.fromId(it.id)?.family }
            .toSet()
            .forEach { platformFamily -> SetIcon(platformFamily) }
    }
}

@Composable
fun SetIcon(platformFamily: PlatformFamily) {
    Image(
        painter = painterResource(when (platformFamily) {
            PlatformFamily.XBOX -> R.drawable.ic_xbox
            PlatformFamily.PLAYSTATION -> R.drawable.ic_play_station
            PlatformFamily.PC -> R.drawable.ic_desktop
            null -> return
        }),
        contentDescription = null,
        modifier = Modifier.size(24.dp).padding(2.dp)
    )
}