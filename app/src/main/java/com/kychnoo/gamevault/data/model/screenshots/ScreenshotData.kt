package com.kychnoo.gamevault.data.model.screenshots

data class ScreenshotData(
    val gameId: Int? = null,
    val count: Int,
    val next: String?,
    val previous: String?,
    val results: List<ScreenshotResultData>
)
