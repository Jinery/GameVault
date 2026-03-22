package com.kychnoo.gamevault.data.remote.api

import com.kychnoo.gamevault.data.remote.dto.response.ApiResponse
import com.kychnoo.gamevault.data.remote.dto.response.GameResponse
import retrofit2.http.GET


interface RawgApi {
    @GET("games")
    suspend fun getAllGames(): ApiResponse<GameResponse>
}