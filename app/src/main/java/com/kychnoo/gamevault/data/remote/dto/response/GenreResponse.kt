package com.kychnoo.gamevault.data.remote.dto.response

import com.kychnoo.gamevault.data.remote.dto.model.genres.GenreDto
import kotlinx.serialization.Serializable

@Serializable
data class GenreResponse(
    val count: Int,
    val next: String?,
    val previous: String?,
    val results: List<GenreDto>
)