package com.kychnoo.gamevault.data.remote.dto.response

import com.kychnoo.gamevault.data.model.GameData
import com.kychnoo.gamevault.data.remote.dto.model.GameDto
import kotlinx.serialization.Serializable

@Serializable
data class GameResponse(

    val count: Int,
    val results: List<GameDto>
)
