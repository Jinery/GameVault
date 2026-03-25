package com.kychnoo.gamevault.data.remote.dto.model

import com.kychnoo.gamevault.data.model.esrb.EsrbRatingData
import com.kychnoo.gamevault.data.remote.dto.enums.EsrbName
import com.kychnoo.gamevault.data.remote.dto.enums.EsrbSlug
import com.kychnoo.gamevault.data.remote.dto.utils.DtoMapper
import kotlinx.serialization.Serializable

@Serializable
data class EsrbRatingDto(
    val id: Int,
    val slug: EsrbSlug,
    val name: EsrbName
) : DtoMapper<EsrbRatingData> {
    override fun toData(): EsrbRatingData = EsrbRatingData(
        id = this.id,
        slug = this.slug,
        name = this.name
    )
}
