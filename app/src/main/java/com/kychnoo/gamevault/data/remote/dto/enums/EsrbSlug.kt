package com.kychnoo.gamevault.data.remote.dto.enums

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class EsrbSlug {
    @SerialName("everyone")
    EVERYONE,

    @SerialName("everyone-10-plus")
    EVERYONE_10_PLUS,

    @SerialName("teen")
    TEEN,

    @SerialName("mature")
    MATURE,

    @SerialName("adults-only")
    ADULTS_ONLY,

    @SerialName("rating-pending")
    RATING_PENDING
}