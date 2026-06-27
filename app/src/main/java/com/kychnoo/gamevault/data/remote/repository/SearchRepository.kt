package com.kychnoo.gamevault.data.remote.repository

import com.kychnoo.gamevault.data.local.dao.SearchHistoryDao
import com.kychnoo.gamevault.data.local.entity.SearchHistoryEntity
import kotlinx.coroutines.flow.Flow

class SearchRepository(
    private val dao: SearchHistoryDao
) {
    fun observeLast10(): Flow<List<SearchHistoryEntity>> = dao.getLast10SearchHistory()

    suspend fun addSearchQuery(query: String) {
        if (query.isBlank()) return

        dao.deleteByQuery(query)

        val entry = SearchHistoryEntity(query = query, timestamp = System.currentTimeMillis())
        dao.insert(entry)
        dao.trimHistory()
    }

    suspend fun deleteEntry(entry: SearchHistoryEntity) = dao.delete(entry)
}