package com.kychnoo.gamevault.data.remote.repository

import com.kychnoo.gamevault.R
import com.kychnoo.gamevault.data.model.RepResult
import com.kychnoo.gamevault.data.model.screenshots.ScreenshotData
import com.kychnoo.gamevault.data.remote.api.RawgApi
import com.kychnoo.gamevault.data.remote.dto.response.ApiResponse
import com.kychnoo.gamevault.provider.AndroidResourceProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RawgScreenshotsRepository(
    private val api: RawgApi,
    private val resourceProvider: AndroidResourceProvider
) {
    // Loading screenshots from rawg API.
    suspend fun getGameScreenshots(gamePk: Int): RepResult<ScreenshotData> {
        // Start loading screenshots in Dispatchers IO context.
        return withContext(Dispatchers.IO) {
            when (val response = api.getScreenshotsForGame(gamePk.toString())) {
                is ApiResponse.Success -> { // If server returns success.
                    val screenshots = response.data.toData().copy(gameId = gamePk)
                    RepResult.Success(screenshots)
                }
                is ApiResponse.ApiError -> { // If server returns error.
                    RepResult.Error(Exception(response.message ?: resourceProvider.getHttpCodeMessage(response.responseCode)))
                }
                is ApiResponse.NetworkError -> { // If networking error.
                    RepResult.Error(response.throwable)
                }
                else -> RepResult.Error(Exception(resourceProvider.getString(R.string.unexpected_state))) // Other errors.
            }
        }
    }
}