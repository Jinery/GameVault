package com.kychnoo.gamevault.data.remote.dto.model.genres

import com.kychnoo.gamevault.data.model.genres.GenreData
import com.kychnoo.gamevault.data.remote.dto.utils.DtoMapper
import kotlinx.serialization.Serializable

@Serializable
data class GenreDto(
    val id: Int,
    val name: String,
    val slug: String
): DtoMapper<GenreData> {
    override fun toData(): GenreData = GenreData(
        id = this.id,
        name = this.name,
        slug = this.slug
    )

}