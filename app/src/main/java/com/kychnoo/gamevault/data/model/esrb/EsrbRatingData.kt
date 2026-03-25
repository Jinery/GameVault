package com.kychnoo.gamevault.data.model.esrb

import com.kychnoo.gamevault.data.remote.dto.enums.EsrbName
import com.kychnoo.gamevault.data.remote.dto.enums.EsrbSlug
import com.kychnoo.gamevault.data.remote.dto.utils.DtoMapper

data class EsrbRatingData(
    val id: Int,
    val slug: EsrbSlug,
    val name: EsrbName
)