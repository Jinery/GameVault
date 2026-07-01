package com.kychnoo.gamevault.data.manager.favorites

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow

interface FavoritesManager {
    suspend fun toggleFavorite(id: Int, name: String, isFavorite: Boolean)
    fun getFavoriteIds(): Flow<List<Int>>

    fun launchToggleFavorite(scope: CoroutineScope, id: Int, name: String, isFavorite: Boolean)
}