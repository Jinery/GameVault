package com.kychnoo.gamevault.data.remote.dto.model.rating

import com.kychnoo.gamevault.data.model.ratings.RatingData
import com.kychnoo.gamevault.data.remote.dto.utils.DtoMapper
import kotlinx.serialization.Serializable

@Serializable
data class RatingDto(
    val id: Int,
    val title: String,
    val count: Int,
    val percent: Float
) : DtoMapper<RatingData> {
    override fun toData(): RatingData = RatingData(
        id = this.id,
        title = this.title,
        count = this.count,
        percent = this.percent
    )

}

fun List<RatingDto>.toData() = map { it.toData() }
