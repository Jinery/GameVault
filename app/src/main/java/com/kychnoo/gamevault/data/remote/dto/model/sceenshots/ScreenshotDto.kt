package com.kychnoo.gamevault.data.remote.dto.model.sceenshots

import com.kychnoo.gamevault.data.model.screenshots.ScreenshotData
import com.kychnoo.gamevault.data.remote.dto.utils.DtoMapper
import kotlinx.serialization.Serializable

@Serializable
data class ScreenshotDto(
    val count: Int,
    val next: String?,
    val previous: String?,
    val results: List<ScreenshotResultDto>
) : DtoMapper<ScreenshotData> {
    override fun toData(): ScreenshotData = ScreenshotData(
        count = count,
        next = next,
        previous = previous,
        results = results.toData()
    )

    fun List<ScreenshotResultDto>.toData() = map { it.toData() }
}
