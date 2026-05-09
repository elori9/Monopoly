package com.example.monopoly.ui.screens

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.monopoly.R
import android.app.Activity
import androidx.compose.ui.Modifier

enum class MenuScreens(@param:StringRes val title: Int) {
    Start(title = R.string.app_name),
    NewGame(title = R.string.NewGame),
    Config(title = R.string.Configuration),
    Help(title = R.string.Help),
}


@Composable
fun AppNavigation() {
    val context = LocalContext.current
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = MenuScreens.Start.name,
    ) {
        // Start menu screen
        composable(MenuScreens.Start.name) {
            MenuScreen(
                onNavigateToConfig = { navController.navigate(MenuScreens.Config.name) },
                onNavigateToNewGame = { navController.navigate(MenuScreens.NewGame.name) },
                onNavigateToHelp = { navController.navigate(MenuScreens.Help.name) },
                onExit = {
                    (context as? Activity)?.finish()
                },
                modifier = Modifier
            )
        }

        // Config
        composable(MenuScreens.Config.name) {
            // TODO
        }

        // Help
        composable(MenuScreens.Help.name) {
            // TODO
        }

        // New Game
        composable(MenuScreens.NewGame.name) {
            // TODO
        }

    }
}