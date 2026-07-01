package com.kychnoo.gamevault.ui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance

val ColorScheme.bottomMenuColor: Color get() = if (isLight()) Whisper else Charcoal

val ColorScheme.cardColor: Color get() = if (isLight()) Whisper else Charcoal

val ColorScheme.ratingRed: Color get() = if (isLight()) Red500 else Red400

val ColorScheme.ratingYellow: Color get() = if (isLight()) Yellow500 else Yellow400

val ColorScheme.ratingGreen: Color get() = if (isLight()) Green500 else Green400

val ColorScheme.success: Color get() = if (isLight()) Green500 else Green600
val ColorScheme.info: Color get() = if (isLight()) Grey100 else Grey200

fun ColorScheme.isLight(): Boolean = this.background.luminance() > 0.5f