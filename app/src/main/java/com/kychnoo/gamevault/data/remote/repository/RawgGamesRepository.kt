package com.kychnoo.gamevault.data.remote.repository

import android.util.Log
import com.kychnoo.gamevault.BuildConfig
import com.kychnoo.gamevault.R
import com.kychnoo.gamevault.data.model.GameData
import com.kychnoo.gamevault.data.model.RepResult
import com.kychnoo.gamevault.data.remote.api.RawgApi
import com.kychnoo.gamevault.data.remote.dto.model.toGameData
import com.kychnoo.gamevault.data.remote.dto.response.ApiResponse
import com.kychnoo.gamevault.data.remote.dto.response.GameResponse
import com.kychnoo.gamevault.provider.AndroidResourceProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.collections.listOf

class RawgGamesRepository(private val api: RawgApi, private val resourceProvider: AndroidResourceProvider) {
    suspend fun fetchAllGames(): RepResult<List<GameData>> {
        return withContext(Dispatchers.IO) {
            processGameResponse(api.getAllGames())
        }
    }

    suspend fun getSuggestedGames(gameId: Int): RepResult<List<GameData>> {
        return if (BuildConfig.IS_ENTERPRISE_API_USER) { // Read about this variable in build.gradle.kts(:app).
            withContext(Dispatchers.IO) {
                val response = api.getSuggestedGames(gameId)
                processGameResponse(api.getSuggestedGames(gameId))
            }
        } else {
            RepResult.Success(listOf())
        }
    }

    private fun processGameResponse(response: ApiResponse<GameResponse>): RepResult<List<GameData>> {
        return when (response) {
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
            else -> RepResult.Error(Exception(resourceProvider.getString(R.string.unexpected_state)))
        }
    }
}