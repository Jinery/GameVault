package com.kychnoo.gamevault.data.remote.dto.response

import com.kychnoo.gamevault.data.remote.dto.model.development.DevelopmentTeamDto
import kotlinx.serialization.Serializable

@Serializable
data class DevelopmentTeamResponse(
    val count: Int,
    val results: List<DevelopmentTeamDto>
)
