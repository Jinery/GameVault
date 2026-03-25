package com.kychnoo.gamevault.data.remote.dto.response

import com.kychnoo.gamevault.data.remote.dto.model.GameDetailDto
import kotlinx.serialization.Serializable

@Serializable
data class GameDetailResponse(
    val game: GameDetailDto
)
