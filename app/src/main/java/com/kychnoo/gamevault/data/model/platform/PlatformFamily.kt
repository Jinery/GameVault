package com.kychnoo.gamevault.data.model.platform

enum class PlatformFamily(val displayName: String) {
    PLAYSTATION("PlayStation" ),
    XBOX("Xbox"),
    PC("PC")
}

fun PlatformData.toFamily(): PlatformFamily? = Platform.fromId(this.id)?.family