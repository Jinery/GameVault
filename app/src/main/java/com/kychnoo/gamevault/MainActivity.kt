package com.kychnoo.gamevault

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.kychnoo.gamevault.data.manager.snackbar.SnackbarManager
import com.kychnoo.gamevault.data.model.types.snackbar.SnackbarTypes
import com.kychnoo.gamevault.ui.screens.FavoriteGamesScreen
import com.kychnoo.gamevault.ui.screens.FavoritesScreenRoute
import com.kychnoo.gamevault.ui.screens.GameDetailRoute
import com.kychnoo.gamevault.ui.screens.GameDetailScreen
import com.kychnoo.gamevault.ui.screens.MainScreen
import com.kychnoo.gamevault.ui.screens.MainScreenRoute
import com.kychnoo.gamevault.ui.screens.SearchScreen
import com.kychnoo.gamevault.ui.screens.SearchScreenRoute
import com.kychnoo.gamevault.ui.theme.GameVaultTheme
import com.kychnoo.gamevault.ui.widgets.MainSnackbar
import com.kychnoo.gamevault.ui.widgets.bottom.BottomBar
import org.koin.compose.koinInject

class MainActivity : ComponentActivity() {
    private fun getRoutePriority(destination: NavDestination?): Int {
        return when {
            destination?.hasRoute<MainScreenRoute>() == true -> 0
            destination?.hasRoute<SearchScreenRoute>() == true -> 1
            destination?.hasRoute<GameDetailRoute>() == true -> 2
            destination?.hasRoute<FavoritesScreenRoute>() == true -> 3
            else -> 4
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GameVaultTheme {
                SharedTransitionLayout(modifier = Modifier.fillMaxSize()) {
                    val navController = rememberNavController()

                    val navBackStackEntry by navController.currentBackStackEntryAsState()

                    val currentDestination = navBackStackEntry?.destination
                    val shouldShowBottomBar =
                        currentDestination?.hasRoute<MainScreenRoute>() == true
                                || currentDestination?.hasRoute<SearchScreenRoute>() == true
                                || currentDestination?.hasRoute<FavoritesScreenRoute>() == true

                    val snackbarHostState = remember { SnackbarHostState() }
                    var currentSnackbarType by remember { mutableStateOf(SnackbarTypes.INFO) }

                    val snackbarManager: SnackbarManager = koinInject()

                    LaunchedEffect(Unit) {
                        snackbarManager.snackbarEvents.collect { event ->
                            currentSnackbarType = event.type
                            snackbarHostState.showSnackbar(message = event.message)
                        }
                    }

                    Scaffold(
                        modifier = Modifier.fillMaxSize(),
                        snackbarHost = {
                            SnackbarHost(hostState = snackbarHostState) { snackbarData ->
                                MainSnackbar(
                                    message = snackbarData.visuals.message,
                                    type = currentSnackbarType,
                                )
                            }
                        },
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
                        NavHost(
                            navController = navController,
                            startDestination = MainScreenRoute,
                            modifier = Modifier.fillMaxSize(),
                            enterTransition = {
                                val initialPriority = getRoutePriority(initialState.destination)
                                val targetPriority = getRoutePriority(targetState.destination)

                                val isForward = if (initialPriority == targetPriority) {
                                    initialState.destination.hasRoute<GameDetailRoute>()
                                } else {
                                    initialPriority < targetPriority
                                }

                                if (isForward) {
                                    slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Start, animationSpec = tween(400)) + fadeIn(animationSpec = tween(400))
                                } else {
                                    slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.End, animationSpec = tween(400)) + fadeIn(animationSpec = tween(400))
                                }
                            },
                            exitTransition = {
                                val initialPriority = getRoutePriority(initialState.destination)
                                val targetPriority = getRoutePriority(targetState.destination)

                                val isForward = if (initialPriority == targetPriority) {
                                    initialState.destination.hasRoute<GameDetailRoute>()
                                } else {
                                    initialPriority < targetPriority
                                }

                                if (isForward) {
                                    slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Start, animationSpec = tween(400)) + fadeOut(animationSpec = tween(400))
                                } else {
                                    slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.End, animationSpec = tween(400)) + fadeOut(animationSpec = tween(400))
                                }
                            },
                            popEnterTransition = {
                                slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.End, animationSpec = tween(400)) + fadeIn(animationSpec = tween(400))
                            },
                            popExitTransition = {
                                slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.End, animationSpec = tween(400)) + fadeOut(animationSpec = tween(400))
                            }
                        ) {
                            composable<MainScreenRoute> {
                                MainScreen(
                                    onDetailClick = { game ->
                                        navController.navigate(
                                            GameDetailRoute(
                                                id = game.id,
                                                imageUrl = game.imageUrl
                                            )
                                        )
                                    },
                                    innerPadding = innerPadding,
                                    sharedTransitionScope = this@SharedTransitionLayout,
                                    animatedVisibilityScope = this@composable
                                )
                            }
                            composable<GameDetailRoute> { backStackEntry ->
                                val route: GameDetailRoute = backStackEntry.toRoute()
                                GameDetailScreen(
                                    id = route.id,
                                    imageUrl = route.imageUrl,
                                    sharedTransitionScope = this@SharedTransitionLayout,
                                    animatedVisibilityScope = this@composable,
                                    onBackClick = { navController.navigateUp() },
                                    backStackEntry = backStackEntry,
                                    onGameDetailClick = { game ->
                                        navController.navigate(
                                            GameDetailRoute(
                                                id = game.id,
                                                imageUrl = game.imageUrl
                                            )
                                        )
                                    }
                                )
                            }
                            composable<SearchScreenRoute> { backStackEntry ->
                                // On use back event.
                                BackHandler {
                                    navController.navigate(MainScreenRoute) {
                                        popUpTo(navController.graph.findStartDestination().id) { // Navigate to main screen and save state for current screen.
                                            saveState = true
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                                SearchScreen(
                                    onDetailClick = { game ->
                                        navController.navigate(
                                            GameDetailRoute(
                                                id = game.id,
                                                imageUrl = game.imageUrl
                                            )
                                        )
                                    },
                                    innerPadding = innerPadding,
                                    sharedTransitionScope = this@SharedTransitionLayout,
                                    animatedVisibilityScope = this@composable,
                                    backStackEntry = backStackEntry
                                )
                            }
                            composable<FavoritesScreenRoute> { backStackEntry ->
                                BackHandler {
                                    navController.navigate(FavoritesScreenRoute) {
                                        popUpTo(backStackEntry.destination.id) {
                                            saveState = true
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                                FavoriteGamesScreen(
                                    innerPadding = innerPadding,
                                    onDetailClick = { game ->
                                        navController.navigate(
                                            GameDetailRoute(
                                                id = game.id,
                                                imageUrl = game.imageUrl
                                            )
                                        )
                                    },
                                    sharedTransitionScope = this@SharedTransitionLayout,
                                    animatedVisibilityScope = this@composable,
                                    backStackEntry = backStackEntry
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}