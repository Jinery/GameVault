package com.kychnoo.gamevault.ui.widgets

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kychnoo.gamevault.data.model.types.snackbar.SnackbarTypes
import com.kychnoo.gamevault.ui.theme.Charcoal
import com.kychnoo.gamevault.ui.theme.Snow
import com.kychnoo.gamevault.ui.theme.info
import com.kychnoo.gamevault.ui.theme.success

@Composable
fun MainSnackbar(
    message: String,
    type: SnackbarTypes,
) {
    val snackbarColor = when (type) {
        SnackbarTypes.SUCCESS -> MaterialTheme.colorScheme.success
        SnackbarTypes.INFO -> MaterialTheme.colorScheme.info
        SnackbarTypes.ERROR -> MaterialTheme.colorScheme.error
    }

    val contentColor = when (type) {
        SnackbarTypes.SUCCESS -> Snow
        SnackbarTypes.INFO -> Charcoal
        SnackbarTypes.ERROR -> MaterialTheme.colorScheme.onError
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp)
            .animateContentSize(),
        colors = CardDefaults.cardColors(
            containerColor = snackbarColor
        ),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = when (type) {
                    SnackbarTypes.SUCCESS -> Icons.Filled.Check
                    SnackbarTypes.INFO -> Icons.Filled.Info
                    SnackbarTypes.ERROR -> Icons.Filled.Error
                },
                contentDescription = "snackbar_type_icons",
                tint = contentColor
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium,
                color = contentColor,
                modifier = Modifier.weight(1F)
            )
        }
    }
}