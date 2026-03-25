package com.kychnoo.gamevault.data.remote.api

import com.kychnoo.gamevault.data.remote.dto.model.GameDetailDto
import com.kychnoo.gamevault.data.remote.dto.response.ApiResponse
import com.kychnoo.gamevault.data.remote.dto.response.GameDetailResponse
import com.kychnoo.gamevault.data.remote.dto.response.GameResponse
import retrofit2.http.GET
import retrofit2.http.Path


interface RawgApi {
    @GET("games")
    suspend fun getAllGames(): ApiResponse<GameResponse>

    @GET("games/{id}")
    suspend fun getGameDetails(@Path("id") gameId: Int): ApiResponse<GameDetailDto>
}