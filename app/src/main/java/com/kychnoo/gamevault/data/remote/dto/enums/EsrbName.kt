package com.kychnoo.gamevault.data.remote.dto.enums

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class EsrbName(val displayName: String) {
    @SerialName("Everyone")
    EVERYONE("Everyone"),

    @SerialName("Everyone 10+")
    EVERYONE_10_PLUS("Everyone 10+"),

    @SerialName("Teen")
    TEEN("Teen"),

    @SerialName("Mature")
    MATURE("Mature"),

    @SerialName("Adults Only")
    ADULTS_ONLY("Adults Only"),

    @SerialName("Rating Pending")
    RATING_PENDING("Rating Pending");

    override fun toString(): String = displayName
}