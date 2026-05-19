package com.kychnoo.gamevault.data.remote.dto.model.sceenshots

import com.kychnoo.gamevault.data.model.screenshots.ScreenshotResultData
import com.kychnoo.gamevault.data.remote.dto.utils.DtoMapper
import kotlinx.serialization.Serializable

@Serializable
data class ScreenshotResultDto(
    val id: Int,
    val image: String,
    val hidden: Boolean = false,
    val width: Int,
    val height: Int,
) : DtoMapper<ScreenshotResultData> {
    override fun toData(): ScreenshotResultData = ScreenshotResultData(
        id = id,
        image = image,
        hidden = hidden,
        width = width,
        height = height,
    )
}
