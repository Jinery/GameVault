package com.kychnoo.gamevault.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.kychnoo.gamevault.data.model.GameData

@Entity(tableName = "favorite_games")
data class FavoriteGameEntity(
    @PrimaryKey val id: Int,
    val gameName: String
) {
    fun toGameData(): GameData = GameData(
        id = this.id,
        title = this.gameName,
        imageUrl = null,
        score = null,
        rating = 0F,
        platforms = emptyList(),
        platformFamilies = emptyList()
    )
}
