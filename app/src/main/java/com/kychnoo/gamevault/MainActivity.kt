package com.kychnoo.gamevault

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.kychnoo.gamevault.ui.screens.GameDetail
import com.kychnoo.gamevault.ui.screens.GameDetailScreen
import com.kychnoo.gamevault.ui.screens.MainScreen
import com.kychnoo.gamevault.ui.screens.MainScreenRoute
import com.kychnoo.gamevault.ui.screens.SearchScreen
import com.kychnoo.gamevault.ui.screens.SearchScreenRoute
import com.kychnoo.gamevault.ui.theme.GameVaultTheme
import com.kychnoo.gamevault.ui.widgets.bottom.BottomBar

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GameVaultTheme {
                val navController = rememberNavController()

                val navBackStackEntry by navController.currentBackStackEntryAsState()

                val currentDestination = navBackStackEntry?.destination
                val shouldShowBottomBar = currentDestination?.hasRoute<MainScreenRoute>() == true

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        AnimatedVisibility(
                            visible = shouldShowBottomBar,
                            enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
                            exit = slideOutVertically(targetOffsetY = { it }) + fadeOut()
                        ) {
                            BottomBar(navController = navController)
                        }
                    }
                ) { innerPadding ->
                    SharedTransitionLayout(modifier = Modifier.fillMaxSize()) {
                        NavHost(
                            navController = navController,
                            startDestination = MainScreenRoute,
                            modifier = Modifier.fillMaxSize()
                        ) {
                            composable<MainScreenRoute> {
                                MainScreen(
                                    onDetailClick = { game ->
                                        navController.navigate(GameDetail(id = game.id, imageUrl = game.imageUrl))
                                    },
                                    innerPadding = innerPadding,
                                    sharedTransitionScope = this@SharedTransitionLayout,
                                    animatedVisibilityScope = this@composable
                                )
                            }

                            composable<GameDetail> { backStackEntry ->
                                val route: GameDetail = backStackEntry.toRoute()
                                GameDetailScreen(
                                    id = route.id,
                                    imageUrl = route.imageUrl,
                                    sharedTransitionScope = this@SharedTransitionLayout,
                                    animatedVisibilityScope = this@composable,
                                    onBackClick = { navController.navigateUp() }
                                )
                            }
                            composable<SearchScreenRoute> {
                                SearchScreen()
                            }
                        }
                    }
                }
            }
        }
    }
}