package com.kychnoo.gamevault.ui.widgets.bottom

import android.graphics.drawable.Icon
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import com.kychnoo.gamevault.R
import com.kychnoo.gamevault.ui.screens.MainScreenRoute
import com.kychnoo.gamevault.ui.screens.SearchScreenRoute
import com.kychnoo.gamevault.ui.theme.bottomMenuColor

@Composable
fun BottomBar(navController: NavController) {
    val navBackStackEntry: State<NavBackStackEntry?> = navController.currentBackStackEntryAsState()
    val currentDestination: NavDestination? = navController.currentDestination

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 25.dp, vertical = 30.dp)
    ) {
        Surface(
            color = MaterialTheme.colorScheme.bottomMenuColor.copy(0.9f),
            shape = RoundedCornerShape(36.dp),
            tonalElevation = 8.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                val items = listOf(
                    Triple(MainScreenRoute, stringResource(R.string.explore), R.drawable.ic_rocket),
                    Triple(SearchScreenRoute, stringResource(R.string.search), R.drawable.ic_search)
                )

                items.forEach { (route, label, iconRes) ->
                    val isSelected: Boolean = currentDestination?.hasRoute(route::class) ?: false

                    NavigationBarItem(
                        selected = isSelected,
                        onClick = {
                            navController.navigate(route) {
                                popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon =  {
                            Icon(
                                painter = painterResource(id = iconRes),
                                contentDescription = label,
                                modifier = Modifier.size(24.dp)
                                // tint = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onPrimary
                            )
                        },
                        label = { Text(label) }
                    )
                }
            }
        }
    }
}