package com.kychnoo.gamevault.data.model.platform

data class PlatformArData(
    val platform: PlatformData,
    val releasedAt: String?,
    val requirements: RequirementsData?
) {
    companion object {
        fun empty(): PlatformArData = PlatformArData(
            PlatformData.empty(),
            null,
            null
        )

        fun xbox(): PlatformArData = PlatformArData(
            PlatformData.xbox(),
            "15.11.2001",
            null
        )

        fun playStation(): PlatformArData = PlatformArData(
            PlatformData.playStation(),
            "3.12.1994",
            null
        )

        fun pc(): PlatformArData = PlatformArData(
            PlatformData.pc(),
            null,
            RequirementsData(minimum = "Windows 10/Ubuntu Linux 26\nAMD Radeon R7\n16GB RAM\nNVIDIA GeForce GTX 1050", recommended = null)
        )
    }
}
