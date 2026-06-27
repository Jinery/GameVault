package com.kychnoo.gamevault.ui.widgets.loading

import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun CircularLoader(modifier: Modifier = Modifier) {
    CircularProgressIndicator(
        modifier = modifier.size(56.dp),
        strokeWidth = 6.dp,
        gapSize = 8.dp
    )
}