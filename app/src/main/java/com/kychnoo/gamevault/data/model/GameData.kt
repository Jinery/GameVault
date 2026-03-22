package com.kychnoo.gamevault.data.model

data class GameData(
    val id: Int,
    val title: String,
    val imageUrl: String,
    val score: Int,
    val rating: Float,
    val isFavorite: Boolean = false
)