package com.kychnoo.gamevault.data.model.gameDetail

import com.kychnoo.gamevault.data.model.addedBy.AddedByStatusData
import com.kychnoo.gamevault.data.model.esrb.EsrbRatingData
import com.kychnoo.gamevault.data.model.platform.PlatformArData
import com.kychnoo.gamevault.data.model.ratings.RatingData
import com.kychnoo.gamevault.data.model.reactions.Reaction

data class GameDetailData(
    val id: Int,
    val slug: String,
    val name: String,
    val nameOriginal: String,
    val description: String,
    val metacritic: Int,
    val metacriticPlatforms: List<GamePlatformMetacriticData>,
    val released: String,
    val updated: String,
    val backgroundImage: String,
    val backgroundImageAdditional: String,
    val website: String,
    val rating: Float,
    val ratings: List<RatingData>,
    val reactions: List<Reaction>,
    val added: Int,
    val addedByStatus: AddedByStatusData,
    val playtime: Int,
    val screenshotsCount: Int,
    val moviesCount: Int,
    val creatorsCount: Int,
    val achievementsCount: Int,
    val parentAchievementsCount: Int,
    val redditUrl: String,
    val redditName: String,
    val redditDescription: String,
    val redditLogo: String,
    val redditCount: Int,
    val twitchCount: Int,
    val youtubeCount: Int,
    val reviewsTextCount: Int,
    val ratingsCount: Int,
    val suggestionsCount: Int,
    val alternativeNames: List<String>,
    val metacriticUrl: String,
    val parentsCount: Int,
    val additionsCount: Int,
    val gameSeriesCount: Int,
    val esrbRating: EsrbRatingData?,
    val platforms: List<PlatformArData>
)
