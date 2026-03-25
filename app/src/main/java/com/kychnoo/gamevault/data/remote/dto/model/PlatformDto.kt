package com.kychnoo.gamevault.data.remote.dto.model

import com.kychnoo.gamevault.data.model.platform.PlatformData
import com.kychnoo.gamevault.data.remote.dto.utils.DtoMapper
import kotlinx.serialization.Serializable

@Serializable
data class PlatformDto(
    val id: Int,
    val slug: String,
    val name: String
    ): DtoMapper<PlatformData> {
    override fun toData(): PlatformData = PlatformData(
        id = this.id,
        slug = this.slug,
        name = this.name
    )
    }
