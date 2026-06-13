package com.kychnoo.gamevault.ui.widgets.website

import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kychnoo.gamevault.R
import com.kychnoo.gamevault.ui.theme.Snow
import androidx.core.net.toUri

@Composable
fun WebsiteButton(
    websiteUrl: String,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val browserNotFoundText = stringResource(R.string.browser_not_found)
    Button(
        onClick = {
            val intent = Intent(Intent.ACTION_VIEW, websiteUrl.toUri()) // Create action view intent.

            intent.resolveActivity(context.packageManager)?.let { // Check if the user has a browser.
                context.startActivity(intent) // Open link in browser.
            } ?: Toast.makeText(context, browserNotFoundText, Toast.LENGTH_SHORT).show() // Else show error(Browser not found).
        },
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = ButtonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = Snow,
            disabledContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.8f),
            disabledContentColor = Snow
        )
    ) {
        Text(
            text = stringResource(R.string.visit_website),
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(vertical = 6.dp)
        )
    }
}

@Preview
@Composable
fun WebsiteButtonPreview() {
    Box(
        modifier = Modifier.width(300.dp).height(200.dp),
        contentAlignment = Alignment.Center
    ) {
        WebsiteButton(
            websiteUrl = "",
            modifier = Modifier.padding(6.dp)
        )
    }
}