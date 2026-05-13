package com.kychnoo.gamevault.data.model.platform

enum class Platform(
    val id: Int,
    val displayName: String,
    val family: PlatformFamily
) {
    PS5(187, "PlayStation 5", PlatformFamily.PLAYSTATION),
    PS4(18, "PlayStation 4", PlatformFamily.PLAYSTATION),
    PS3(16, "PlayStation 3", PlatformFamily.PLAYSTATION),
    PS2(15, "PlayStation 2", PlatformFamily.PLAYSTATION),
    PS1(27, "PlayStation", PlatformFamily.PLAYSTATION),
    VITA(19, "PlayStation Vita", PlatformFamily.PLAYSTATION),
    PSP(17, "PSP", PlatformFamily.PLAYSTATION),

    XBOX_SERIES(169, "Xbox Series X/S", PlatformFamily.XBOX),
    XBOX_ONE(9, "Xbox One", PlatformFamily.XBOX),
    XBOX_360(14, "Xbox 360", PlatformFamily.XBOX),
    XBOX(13, "Xbox", PlatformFamily.XBOX),

    LINUX(6, "Linux", PlatformFamily.PC),
    MACOS(5, "macOS", PlatformFamily.PC),
    PC(4, "Windows", PlatformFamily.PC);

    companion object {
        private val mapById = entries.associateBy { it.id }

        fun fromId(id: Int): Platform? = mapById[id]

        fun isPlayStation(id: Int): Boolean =
            mapById[id]?.family == PlatformFamily.PLAYSTATION

        fun isXbox(id: Int): Boolean =
            mapById[id]?.family == PlatformFamily.XBOX

        fun isPC(id: Int): Boolean =
            mapById[id]?.family == PlatformFamily.PC

        fun getByFamily(family: PlatformFamily): List<Platform> =
            entries.filter { it.family == family }
    }
}