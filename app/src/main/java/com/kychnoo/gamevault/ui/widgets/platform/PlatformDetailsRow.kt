package com.kychnoo.gamevault.ui.widgets.platform

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SecondaryScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kychnoo.gamevault.R
import com.kychnoo.gamevault.data.model.platform.PlatformArData
import com.kychnoo.gamevault.data.model.platform.PlatformFamily
import com.kychnoo.gamevault.data.model.platform.toFamily
import kotlinx.coroutines.launch

@Composable
fun PlatformDetailsRow(
    platforms: List<PlatformArData>,
    modifier: Modifier = Modifier) {
    // Extract unique platform families and sort by enum order for stable UI.
    val platformFamilies = remember(platforms) {
        platforms.mapNotNull { it.platform.toFamily() }.distinct().sortedBy { it.ordinal }
    }

    val pagerState = rememberPagerState { platformFamilies.size }
    val coroutineScope = rememberCoroutineScope()

    Column(modifier = modifier.wrapContentHeight()) {
        Text(
            text = stringResource(R.string.platforms_and_requirements_title),
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(Modifier.height(12.dp))

        SecondaryScrollableTabRow(
            selectedTabIndex = pagerState.currentPage,
            edgePadding = 16.dp,
            containerColor = Color.Transparent,
            contentColor = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.fillMaxWidth(),
            indicator = {
                TabRowDefaults.SecondaryIndicator(
                    Modifier.tabIndicatorOffset(pagerState.currentPage),
                    color = MaterialTheme.colorScheme.primary
                )
            }
        ) {
            platformFamilies.forEachIndexed { index, family ->
                val isSelected = pagerState.currentPage == index

                // Animate tab text color on selection.
                val textColor = animateColorAsState(
                    targetValue = if (isSelected) MaterialTheme.colorScheme.primary else Color.Gray,
                    label = "TabTextColor"
                )

                Tab(
                    selected = isSelected,
                    onClick = {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(index)
                        }
                    },
                    text = {
                        Text(
                            text = family.displayName,
                            color = textColor.value,
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                )
            }
        }

        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .animateContentSize(
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioNoBouncy,
                        stiffness = Spring.StiffnessLow
                    )
                ),
            verticalAlignment = Alignment.Top
        ) { page ->
            val currentFamily = platformFamilies[page]
            // Filter platforms that belong to the selected family.
            PlatformDetailContent(
                family = currentFamily,
                platforms = platforms.filter { it.platform.toFamily() == currentFamily }
            )
        }
    }
}

@Composable
fun PlatformDetailContent(
    family: PlatformFamily,
    platforms: List<PlatformArData>
) {
    when (family) {
        PlatformFamily.PC -> {
            val pc = platforms.first()
            PlatformRequirements(pc)
        }
        else -> ConsoleRequirements(family, platforms) }
}

@Composable
fun ConsoleRequirements(
    consoleFamily: PlatformFamily,
    consoles: List<PlatformArData>
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = stringResource(
                R.string.supported_consoles,
                consoleFamily.displayName,
                consoles.joinToString("\n") { it.platform.name }
            ),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(16.dp)
        )
    }
}

@Composable
fun PlatformRequirements(platform: PlatformArData) {
    val requirements = platform.requirements

    if (requirements != null) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = requirements.minimum?: stringResource(R.string.min_requirements_not_specified),
                    fontWeight = FontWeight.Normal,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(16.dp)
                )
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = requirements.recommended ?: stringResource(R.string.recom_requirements_not_specified),
                    fontWeight = FontWeight.Normal,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
    } else {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
        ) {
            Text(
                text = stringResource(R.string.dont_have_requirements, platform.platform.name),
                fontSize = 16.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}