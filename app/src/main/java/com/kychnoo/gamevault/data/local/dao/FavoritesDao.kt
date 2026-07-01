package com.kychnoo.gamevault.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.kychnoo.gamevault.data.local.entity.FavoriteGameEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoritesDao {
    @Query("SELECT id FROM favorite_games")
    fun getFavoriteIdsFlow(): Flow<List<Int>>

    @Query("SELECT * FROM favorite_games")
    suspend fun getAllFavorites(): List<FavoriteGameEntity>

    @Insert
    suspend fun insert(favoriteGame: FavoriteGameEntity)

    @Query("DELETE FROM favorite_games WHERE id = :id")
    suspend fun deleteFavoriteGameById(id: Int)
}