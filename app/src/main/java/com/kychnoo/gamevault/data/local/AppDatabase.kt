package com.kychnoo.gamevault.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.kychnoo.gamevault.data.local.dao.SearchHistoryDao
import com.kychnoo.gamevault.data.local.entity.SearchHistoryEntity

@Database(entities = [SearchHistoryEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun searchHistoryDao(): SearchHistoryDao
}