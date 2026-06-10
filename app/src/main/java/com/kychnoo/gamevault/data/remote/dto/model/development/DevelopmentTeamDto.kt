package com.kychnoo.gamevault.data.remote.dto.model.development

import com.kychnoo.gamevault.data.model.development.DevelopmentTeamData
import com.kychnoo.gamevault.data.remote.dto.utils.DtoMapper
import kotlinx.serialization.Serializable

@Serializable
data class DevelopmentTeamDto(
    val id: Int,
    val name: String,
    val slug: String,
    val image: String?,
    val image_background: String,
    val games_count: Int
) : DtoMapper<DevelopmentTeamData> {
    override fun toData(): DevelopmentTeamData = DevelopmentTeamData(
        id = this.id,
        name = this.name,
        slug = this.slug,
        image = this.image,
        imageBackground = image_background,
        gamesCount = games_count
    )

}
