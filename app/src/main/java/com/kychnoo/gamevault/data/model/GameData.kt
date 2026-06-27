package com.kychnoo.gamevault.data.model

import androidx.compose.runtime.Immutable
import com.kychnoo.gamevault.data.model.platform.PlatformArData
import com.kychnoo.gamevault.data.model.platform.PlatformFamily

@Immutable
data class GameData(
    val id: Int,
    val title: String,
    val imageUrl: String?,
    val score: Int?,
    val rating: Float,
    val platforms: List<PlatformArData>,
    val platformFamilies: List<PlatformFamily>,
    val isFavorite: Boolean = false
) {
    companion object {
        fun testPlatforms(): List<PlatformArData> {
            return listOf(
                PlatformArData.playStation(),
                PlatformArData.xbox()
            )
        }

        fun testFamilies(): List<PlatformFamily> {
            return listOf(
                PlatformFamily.PLAYSTATION,
                PlatformFamily.XBOX
            )
        }
    }
}