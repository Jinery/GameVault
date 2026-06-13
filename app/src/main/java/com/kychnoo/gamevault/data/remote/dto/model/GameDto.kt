package com.kychnoo.gamevault.data.remote.dto.model

import com.kychnoo.gamevault.data.model.GameData
import kotlinx.serialization.Serializable

@Serializable
data class GameDto(
    val id: Int,
    val name: String,
    val background_image: String,
    val rating: Float,
    val metacritic: Int?,
    val platforms: List<PlatformArDto>
)

fun  GameDto.toGameData() = GameData(
    id = this.id,
    title = this.name,
    imageUrl = this.background_image,
    score = this.metacritic,
    rating = this.rating,
    platforms = this.platforms.toData(),
    isFavorite = false
)
