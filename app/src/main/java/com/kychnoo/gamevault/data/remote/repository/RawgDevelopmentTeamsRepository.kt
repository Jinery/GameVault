package com.kychnoo.gamevault.data.remote.repository

import com.kychnoo.gamevault.R
import com.kychnoo.gamevault.data.model.RepResult
import com.kychnoo.gamevault.data.model.development.DevelopmentTeamPageData
import com.kychnoo.gamevault.data.remote.api.RawgApi
import com.kychnoo.gamevault.data.remote.dto.response.ApiResponse
import com.kychnoo.gamevault.provider.AndroidResourceProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RawgDevelopmentTeamsRepository(private val api: RawgApi, private val resourceProvider: AndroidResourceProvider) {
    suspend fun getDevelopmentTeamForGame(gamePk: Int): RepResult<DevelopmentTeamPageData> {
        return withContext(Dispatchers.IO) {
            when (val response = api.getDevelopmentTeamForGame(gamePk.toString())) {
                is ApiResponse.Success -> {
                    val devTeams = response.data.results.map { it.toData() }
                    RepResult.Success(DevelopmentTeamPageData(
                        gameId = gamePk,
                        count = response.data.count,
                        results = devTeams)
                    )
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