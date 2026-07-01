package com.kychnoo.gamevault.data.remote.repository

import com.kychnoo.gamevault.data.model.RepResult
import com.kychnoo.gamevault.data.remote.api.RawgApi
import com.kychnoo.gamevault.data.remote.dto.response.ApiResponse
import com.kychnoo.gamevault.data.remote.dto.response.GameResponse
import com.kychnoo.gamevault.provider.AndroidResourceProvider
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class RawgGamesRepositoryTest {
    private val api = mockk<RawgApi>()
    private val resourceProvider = mockk<AndroidResourceProvider>()
    private val repository = RawgGamesRepository(api, resourceProvider)

    @Test
    fun `fetchAllGames returns success when api call is successful`() = runTest {
        val mockResponse = GameResponse(count = 0, results = emptyList())
        coEvery { api.getAllGames() } returns ApiResponse.Success(mockResponse, 200)

        val result = repository.fetchAllGames()

        assertTrue(result is RepResult.Success)
        assertEquals(0, (result as RepResult.Success).data.size)
    }

    @Test
    fun `fetchAllGames returns error when api call fails with network error`() = runTest {
        val exception = Exception("Network failure")
        coEvery { api.getAllGames() } returns ApiResponse.NetworkError(exception)

        val result = repository.fetchAllGames()

        assertTrue(result is RepResult.Error)
        assertEquals(exception, (result as RepResult.Error).exception)
    }

    @Test
    fun `fetchAllGames returns error with message when api call fails with api error`() = runTest {
        val errorMessage = "Not Found"
        coEvery { api.getAllGames() } returns ApiResponse.ApiError(errorMessage, 404)

        val result = repository.fetchAllGames()

        assertTrue(result is RepResult.Error)
        assertEquals(errorMessage, (result as RepResult.Error).exception.message)
    }
}
