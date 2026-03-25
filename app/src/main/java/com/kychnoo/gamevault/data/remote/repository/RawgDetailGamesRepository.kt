package com.kychnoo.gamevault.data.remote.repository

import com.kychnoo.gamevault.R
import com.kychnoo.gamevault.data.model.RepResult
import com.kychnoo.gamevault.data.model.gameDetail.GameDetailData
import com.kychnoo.gamevault.data.remote.api.RawgApi
import com.kychnoo.gamevault.data.remote.dto.model.GameDetailDto
import com.kychnoo.gamevault.data.remote.dto.model.toGameDetailData
import com.kychnoo.gamevault.data.remote.dto.response.ApiResponse
import com.kychnoo.gamevault.data.remote.dto.response.GameDetailResponse
import com.kychnoo.gamevault.provider.AndroidResourceProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RawgDetailGamesRepository(private val api: RawgApi, private val resourceProvider: AndroidResourceProvider) {
    suspend fun getGameDetails(gameId: Int): RepResult<GameDetailData> {
        return withContext(Dispatchers.IO) {
            when (val response = api.getGameDetails(gameId)) {
                is ApiResponse.Success<GameDetailDto> -> {
                    val details = response.data.toGameDetailData()
                    RepResult.Success(details)
                }
                is ApiResponse.ApiError -> {
                    RepResult.Error(Exception(response.message ?: resourceProvider.getHttpCodeMessage(response.responseCode)))
                }
                is ApiResponse.NetworkError -> {
                    RepResult.Error(response.throwable)
                }
                else -> RepResult.Error(Exception(resourceProvider.getString(R.string.unexpected_state)))
            }
        }
    }
}