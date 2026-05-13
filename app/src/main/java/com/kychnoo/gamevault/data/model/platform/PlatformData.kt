package com.kychnoo.gamevault.data.model.platform

data class PlatformData(
    val id: Int,
    val slug: String,
    val name: String
) {
    companion object {
        fun empty(): PlatformData = PlatformData(-1, "", "")

        fun xbox(): PlatformData = PlatformData(13, "Xbox", "xbox")

        fun playStation(): PlatformData = PlatformData(27, "PlayStation", "playstation")
    }
}
