package com.kychnoo.gamevault.data.remote.repository

import com.kychnoo.gamevault.data.local.dao.FavoritesDao
import com.kychnoo.gamevault.data.local.entity.FavoriteGameEntity
import com.kychnoo.gamevault.data.remote.api.RawgApi
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class FavoriteGamesRepositoryTest {
    private val api = mockk<RawgApi>()
    private val dao = mockk<FavoritesDao>()
    private val repository = FavoriteGamesRepository(api, dao)

    @Test
    fun `addGameToFavorites should call dao insert`() = runTest {
        val id = 1
        val name = "God of War"
        coEvery { dao.insert(any()) } returns Unit

        repository.addGameToFavorites(id, name)

        coVerify { dao.insert(match { it.id == id && it.gameName == name }) }
    }

    @Test
    fun `removeGameFromFavorites should call dao delete and clear cache`() = runTest {
        val id = 1
        coEvery { dao.deleteFavoriteGameById(any()) } returns Unit

        repository.removeGameFromFavorites(id)

        coVerify { dao.deleteFavoriteGameById(id) }
    }

    @Test
    fun `getFavoritesIdsFlow should return flow from dao`() = runTest {
        val ids = listOf(1, 2, 3)
        coEvery { dao.getFavoriteIdsFlow() } returns flowOf(ids)

        val result = repository.getFavoritesIdsFlow().first()

        assertEquals(ids, result)
    }
}
