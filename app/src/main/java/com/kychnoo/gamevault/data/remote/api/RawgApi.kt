package com.kychnoo.gamevault.data.remote.api

import com.kychnoo.gamevault.data.remote.dto.model.GameDetailDto
import com.kychnoo.gamevault.data.remote.dto.model.sceenshots.ScreenshotDto
import com.kychnoo.gamevault.data.remote.dto.response.ApiResponse
import com.kychnoo.gamevault.data.remote.dto.response.DevelopmentTeamResponse
import com.kychnoo.gamevault.data.remote.dto.response.GameDetailResponse
import com.kychnoo.gamevault.data.remote.dto.response.GameResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface RawgApi {
    @GET("games")
    suspend fun getAllGames(): ApiResponse<GameResponse>

    @GET("games/{id}")
    suspend fun getGameDetails(@Path("id") gameId: Int): ApiResponse<GameDetailDto>

    @GET("games/{game_pk}/screenshots")
    suspend fun getScreenshotsForGame(
        @Path("game_pk") gamePk: String,
        @Query("page_size") pageSize: Int = 15
    ): ApiResponse<ScreenshotDto>

    @GET("games/{game_pk}/development-team")
    suspend fun getDevelopmentTeamForGame(
        @Path("game_pk") gamePk: String,
    ): ApiResponse<DevelopmentTeamResponse>

    @GET("games/{id}/suggested")
    suspend fun getSuggestedGames(@Path("id") gameId: Int): ApiResponse<GameResponse>
}