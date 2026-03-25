package com.kychnoo.gamevault.data.remote.dto.model

import com.kychnoo.gamevault.data.model.platform.PlatformArData
import com.kychnoo.gamevault.data.model.platform.PlatformData
import com.kychnoo.gamevault.data.remote.dto.utils.DtoMapper
import kotlinx.serialization.Serializable

@Serializable
data class PlatformArDto(
    val platform: PlatformDto,
    val released_at: String?,
    val requirements: RequirementsDto?

) : DtoMapper<PlatformArData> {
    override fun toData(): PlatformArData = PlatformArData(
        platform = platform.toData(),
        releasedAt = released_at,
        requirements = requirements?.toData()
    )

}

fun List<PlatformArDto>.toData() = map { it.toData() }