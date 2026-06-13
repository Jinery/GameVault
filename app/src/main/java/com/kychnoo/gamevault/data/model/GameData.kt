package com.kychnoo.gamevault.data.model

import androidx.compose.runtime.Immutable
import com.kychnoo.gamevault.data.model.platform.PlatformArData

@Immutable
data class GameData(
    val id: Int,
    val title: String,
    val imageUrl: String,
    val score: Int?,
    val rating: Float,
    val platforms: List<PlatformArData>,
    val isFavorite: Boolean = false
)