package com.kychnoo.gamevault.data.remote.dto.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ReactionsDto(
    @SerialName("1") val fire: Int? = null,           // 🔥
    @SerialName("2") val thumbsUp: Int? = null,       // 👍
    @SerialName("3") val thumbsDown: Int? = null,     // 👎
    @SerialName("4") val mindBlown: Int? = null,      // 🤯
    @SerialName("5") val rocket: Int? = null,         // 🚀
    @SerialName("6") val bug: Int? = null,            // 🐛
    @SerialName("7") val poop: Int? = null,           // 💩
    @SerialName("8") val heart: Int? = null,          // ❤️
    @SerialName("9") val gamepad: Int? = null,        // 🎮
    @SerialName("10") val target: Int? = null,        // 🎯
    @SerialName("11") val money: Int? = null,         // 💰
    @SerialName("12") val clock: Int? = null,         // 🕐
    @SerialName("13") val sleepy: Int? = null,        // 😴
    @SerialName("14") val art: Int? = null,           // 🎨
    @SerialName("15") val music: Int? = null,         // 🎵
    @SerialName("16") val book: Int? = null,          // 📖
    @SerialName("18") val trophy: Int? = null,        // 🏆
    @SerialName("20") val handshake: Int? = null,     // 🤝
    @SerialName("21") val globe: Int? = null          // 🌐
)