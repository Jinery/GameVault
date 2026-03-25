package com.kychnoo.gamevault.data.remote.dto.model

import com.kychnoo.gamevault.data.model.platform.RequirementsData
import com.kychnoo.gamevault.data.remote.dto.utils.DtoMapper
import kotlinx.serialization.Serializable

@Serializable
data class RequirementsDto(
    val minimum: String? = null,
    val recommended: String? = null
) : DtoMapper<RequirementsData> {
    override fun toData(): RequirementsData = RequirementsData(
        minimum = this.minimum,
        recommended = this.recommended
    )

}


