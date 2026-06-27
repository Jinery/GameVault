package com.kychnoo.gamevault.ui.widgets.platform

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.kychnoo.gamevault.R
import com.kychnoo.gamevault.data.model.platform.PlatformFamily

@Composable
fun PlatformsRow(
    platformFamilies: List<PlatformFamily>,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.padding(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        platformFamilies.forEach { platformFamily -> SetIcon(platformFamily) }
    }
}

@Composable
fun SetIcon(platformFamily: PlatformFamily) {
    Icon(
        painter = painterResource(when (platformFamily) {
            PlatformFamily.XBOX -> R.drawable.ic_xbox
            PlatformFamily.PLAYSTATION -> R.drawable.ic_play_station
            PlatformFamily.PC -> R.drawable.ic_desktop
        }),
        contentDescription = null,
        modifier = Modifier.size(24.dp).padding(2.dp)
    )
}