package com.kychnoo.gamevault

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.kychnoo.gamevault.ui.screens.MainScreen
import com.kychnoo.gamevault.ui.screens.MainScreenRoute
import com.kychnoo.gamevault.ui.theme.GameVaultTheme
import com.kychnoo.gamevault.ui.widgets.bottom.BottomBar

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GameVaultTheme {
                val navController = rememberNavController()

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        BottomBar(navController = navController)
                    }
                ) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = MainScreenRoute,
                    ) {
                        composable<MainScreenRoute> {
                            MainScreen(onDetailClick = {  }, innerPadding = innerPadding)
                        }
                    }
                }
            }
        }
    }
}