package com.kychnoo.gamevault.data.remote.repository

import com.kychnoo.gamevault.data.model.GameData
import com.kychnoo.gamevault.data.model.RepResult
import com.kychnoo.gamevault.data.remote.api.RawgApi
import com.kychnoo.gamevault.data.remote.dto.model.toGameData
import com.kychnoo.gamevault.data.remote.dto.response.ApiResponse
import com.kychnoo.gamevault.provider.AndroidResourceProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RawgGamesRepository(private val api: RawgApi, private val resourceProvider: AndroidResourceProvider) {
    suspend fun fetchAllGames(): RepResult<List<GameData>> {
        return withContext(Dispatchers.IO) {
            when (val response = api.getAllGames()) {
                is ApiResponse.Success -> {
                    val games = response.data.results.map { it.toGameData() }
                    RepResult.Success(games)
                }
                is ApiResponse.ApiError -> {
                    RepResult.Error(Exception(response.message ?: resourceProvider.getHttpCodeMessage(response.responseCode)))
                }
                is ApiResponse.NetworkError -> {
                    RepResult.Error(response.throwable)
                }
                else -> RepResult.Error(Exception("Unexpected state"))
            }
        }
    }
}