package com.kychnoo.gamevault.data.remote.repository

import com.kychnoo.gamevault.R
import com.kychnoo.gamevault.data.model.RepResult
import com.kychnoo.gamevault.data.model.genres.GenreData
import com.kychnoo.gamevault.data.remote.api.RawgApi
import com.kychnoo.gamevault.data.remote.dto.response.ApiResponse
import com.kychnoo.gamevault.provider.AndroidResourceProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RawgGenresRepository(private val api: RawgApi, private val resourceProvider: AndroidResourceProvider) {
    suspend fun getAllGenres(): RepResult<List<GenreData>> {
        return withContext(Dispatchers.IO) {
            when (val response = api.getGenres()) {
                is ApiResponse.Success -> {
                    val genres = response.data.results.map { it.toData() }
                    RepResult.Success(genres)
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