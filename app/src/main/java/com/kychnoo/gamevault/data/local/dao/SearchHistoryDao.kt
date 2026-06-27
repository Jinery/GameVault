package com.kychnoo.gamevault.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.kychnoo.gamevault.data.local.entity.SearchHistoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SearchHistoryDao {
    @Query("SELECT * FROM search_history ORDER BY timestamp DESC LIMIT 10")
    fun getLast10SearchHistory(): Flow<List<SearchHistoryEntity>>

    @Insert
    suspend fun insert(searchEntity: SearchHistoryEntity)

    @Delete
    suspend fun delete(searchEntity: SearchHistoryEntity)

    @Query("DELETE FROM search_history WHERE `query` = :query")
    suspend fun deleteByQuery(query: String)

    @Query("DELETE FROM search_history WHERE id NOT IN (SELECT id FROM search_history ORDER BY timestamp DESC LIMIT 10)")
    suspend fun trimHistory()

    @Query("DELETE FROM search_history")
    suspend fun clearAll()
}