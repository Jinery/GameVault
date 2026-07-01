package com.kychnoo.gamevault.data.remote.dto.model

import com.kychnoo.gamevault.data.model.GameData
import com.kychnoo.gamevault.data.model.platform.toFamily
import com.kychnoo.gamevault.data.remote.dto.utils.DtoMapper
import kotlinx.serialization.Serializable

@Serializable
data class GameDto(
    val id: Int,
    val name: String,
    val background_image: String?,
    val rating: Float,
    val metacritic: Int?,
    val platforms: List<PlatformArDto>
) : DtoMapper<GameData> {
    override fun toData(): GameData = GameData(
        id = this.id,
        title = this.name,
        imageUrl = this.background_image,
        score = this.metacritic,
        rating = this.rating,
        platforms = this.platforms.toData(),
        platformFamilies = this.platforms
            .mapNotNull { it.platform.toData().toFamily() }
            .distinct()
            .sortedBy { it.ordinal }
    )
}
