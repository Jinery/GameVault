package com.kychnoo.gamevault.ui.widgets.search.history

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kychnoo.gamevault.R

@Composable
fun HistoryItem(
    query: String,
    onDelete: () -> Unit,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(40.dp)
            .clickable(onClick = onClick),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_history),
            contentDescription = "history_item_icon",
            tint = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier
                .size(32.dp)
                .padding(start = 12.dp)
        )
        Text(
            text = query,
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center
        )
        Icon(
            painter = painterResource(R.drawable.ic_delete),
            contentDescription = "delete_history_item_button",
            tint = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier
                .size(32.dp)
                .padding(end = 12.dp)
                .clickable(onClick = onDelete)
        )
    }
}

@Preview
@Composable
private fun HistoryItemPreview() {
    HistoryItem("Ok", {}, {})
}