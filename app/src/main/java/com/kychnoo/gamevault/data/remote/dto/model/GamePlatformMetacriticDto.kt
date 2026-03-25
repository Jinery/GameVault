package com.kychnoo.gamevault.data.remote.dto.model

import com.kychnoo.gamevault.data.model.gameDetail.GamePlatformMetacriticData
import com.kychnoo.gamevault.data.remote.dto.utils.DtoMapper
import kotlinx.serialization.Serializable

@Serializable
data class GamePlatformMetacriticDto(
    val metascore: Int,
    val url: String
) : DtoMapper<GamePlatformMetacriticData> {
    override fun toData(): GamePlatformMetacriticData = GamePlatformMetacriticData(
        metascore = this.metascore,
        url = this.url
    )

}

fun List<GamePlatformMetacriticDto>.toData() = map { it.toData() }