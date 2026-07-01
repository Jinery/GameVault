package com.kychnoo.gamevault.data.manager.favorites

import com.kychnoo.gamevault.data.remote.repository.FavoriteGamesRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class FavoritesManagerImp(
    private val repository: FavoriteGamesRepository
) : FavoritesManager {
    override suspend fun toggleFavorite(id: Int, name: String, isFavorite: Boolean) {
        if (!isFavorite) {
            repository.addGameToFavorites(id, name)
        } else {
            repository.removeGameFromFavorites(id)
        }
    }

    override fun getFavoriteIds(): Flow<List<Int>> = repository.getFavoritesIdsFlow()
    override fun launchToggleFavorite(
        scope: CoroutineScope,
        id: Int,
        name: String,
        isFavorite: Boolean
    ) {
        scope.launch {
            toggleFavorite(id, name, isFavorite)
        }
    }
}