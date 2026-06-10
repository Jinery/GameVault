package com.kychnoo.gamevault.data.model.development

data class DevelopmentTeamData(
    val id: Int,
    val name: String,
    val slug: String,
    val image: String?,
    val imageBackground: String,
    val gamesCount: Int
) {
    companion object {
        val PreviewDevTeam = DevelopmentTeamData(
            id = 0,
            name = "OneNote Team",
            slug = "onenote_team",
            image = "",
            imageBackground = "",
            gamesCount = 7
        )
    }
}
