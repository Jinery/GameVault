package com.kychnoo.gamevault.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.kychnoo.gamevault.data.local.dao.FavoritesDao
import com.kychnoo.gamevault.data.local.dao.SearchHistoryDao
import com.kychnoo.gamevault.data.local.entity.FavoriteGameEntity
import com.kychnoo.gamevault.data.local.entity.SearchHistoryEntity

@Database(entities = [SearchHistoryEntity::class, FavoriteGameEntity::class], version = 2, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun searchHistoryDao(): SearchHistoryDao
    abstract fun favoritesDao(): FavoritesDao

    companion object {
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("""
                    CREATE TABLE IF NOT EXISTS favorite_games (
                        id INTEGER PRIMARY KEY NOT NULL,
                        gameName TEXT NOT NULL
                    )
                """)
            }
        }
    }
}