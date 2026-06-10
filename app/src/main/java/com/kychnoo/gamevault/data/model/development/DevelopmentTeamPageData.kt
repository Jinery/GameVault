package com.kychnoo.gamevault.data.model.development

data class DevelopmentTeamPageData(
    val gameId: Int,
    val count: Int,
    val results: List<DevelopmentTeamData>
) {
    companion object {
        val PreviewDevTeamPageData = DevelopmentTeamPageData(
            gameId = 0,
            count = 2,
            results = listOf(DevelopmentTeamData.PreviewDevTeam, DevelopmentTeamData.PreviewDevTeam)
        )
    }
}