package com.kychnoo.gamevault.data.remote.repository

import android.util.Log
import com.kychnoo.gamevault.data.local.dao.FavoritesDao
import com.kychnoo.gamevault.data.local.entity.FavoriteGameEntity
import com.kychnoo.gamevault.data.model.GameData
import com.kychnoo.gamevault.data.model.RepResult
import com.kychnoo.gamevault.data.model.states.FavoritesGamesState
import com.kychnoo.gamevault.data.remote.api.RawgApi
import com.kychnoo.gamevault.data.remote.dto.response.ApiResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.supervisorScope
import kotlinx.coroutines.withContext
import kotlin.collections.emptyList
import kotlin.collections.map

class FavoriteGamesRepository(
    private val api: RawgApi,
    private val dao: FavoritesDao,
) {
    private val _cache = MutableStateFlow<Map<Int, GameData>>(emptyMap())
    private val cache: StateFlow<Map<Int, GameData>> = _cache.asStateFlow()

    fun getFavoritesIdsFlow(): Flow<List<Int>> = dao.getFavoriteIdsFlow()

    fun getFavoriteGamesStateFlow(): Flow<FavoritesGamesState> {
        return dao.getFavoriteIdsFlow().combine(cache) { ids, cacheMap ->
            val games = ids.mapNotNull { id -> cacheMap[id] }

            if (games.size == ids.size) {
                FavoritesGamesState(
                    games = games.map { it.copy(isFavorite = true) },
                    errorMessage = null
                )
            } else {
                FavoritesGamesState(
                    games = games.map { it.copy(isFavorite = true) },
                    errorMessage = null,
                    missingIds = ids.filter { !cacheMap.containsKey(it) }
                )
            }
        }.flowOn(Dispatchers.IO)
    }

    suspend fun addGameToFavorites(id: Int, name: String) {
        dao.insert(FavoriteGameEntity(id, name))
    }

    suspend fun removeGameFromFavorites(id: Int) {
        dao.deleteFavoriteGameById(id)
        _cache.update { it.minus(id) }
    }

    suspend fun fetchFavoritesGames(forceRefresh: Boolean = false): RepResult<FavoritesGamesState> {
        return withContext(Dispatchers.IO) {
            val localFavorites = dao.getAllFavorites()

            if (localFavorites.isEmpty()) {
                return@withContext RepResult.Success(
                    FavoritesGamesState(
                        games = emptyList(),
                        errorMessage = null
                    )
                )
            }

            if (forceRefresh) _cache.update { emptyMap() }

            val ids = localFavorites.map { it.id }
            val missingIds = ids.filter { !_cache.value.containsKey(it) }

            if (missingIds.isEmpty()) {
                val games = ids.mapNotNull { _cache.value[it] }
                return@withContext RepResult.Success(
                    FavoritesGamesState(
                        games = games.map { it.copy(isFavorite = true) },
                        errorMessage = null
                    )
                )
            }

            return@withContext when (val apiResult = fetchGamesByIds(missingIds)) {
                is RepResult.Success<List<GameData>> -> {
                    val games = apiResult.data
                    RepResult.Success(
                        FavoritesGamesState(
                            games = games.map { it.copy(isFavorite = true) },
                            errorMessage = null
                        )
                    )
                }
                is RepResult.Error -> {
                    RepResult.Success(
                        FavoritesGamesState(
                            games = localFavorites.map { it.toGameData().copy(isFavorite = true) },
                            errorMessage = apiResult.exception.message
                        )
                    )
                }
            }

        }
    }

    suspend fun fetchGamesByIds(ids: List<Int>): RepResult<List<GameData>>
        = loadBatchesGames(ids)

    private suspend fun loadBatchesGames(ids: List<Int>, batchSize: Int = 10): RepResult<List<GameData>> {
        val results = mutableListOf<GameData>()
        var lastError: String? = null

        supervisorScope {
            ids.chunked(batchSize).forEach { batch ->
                val batchResults = batch.map { id ->
                    async {
                        try {
                            when (val response = api.getGameDetails(id)) {
                                is ApiResponse.Success -> {
                                    val game = response.data.toGameData()
                                    _cache.update { it.plus(id to game)}
                                    game
                                }
                                is ApiResponse.ApiError -> {
                                    lastError = response.message
                                    null
                                }
                                is ApiResponse.NetworkError -> {
                                    lastError = response.throwable.message
                                    null
                                }
                                is ApiResponse.Loading -> null
                            }
                        } catch (e: Exception) {
                            lastError = e.message
                            null
                        }
                    }
                }.awaitAll().filterNotNull()

                results.addAll(batchResults)
            }
        }

        return if (results.isEmpty() && lastError != null) {
            RepResult.Error(Exception(lastError))
        } else {
            RepResult.Success(results)
        }
    }
}