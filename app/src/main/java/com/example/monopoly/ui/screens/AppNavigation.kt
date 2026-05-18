package com.example.monopoly.ui.screens

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.monopoly.R
import android.app.Activity
import android.app.Application
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.monopoly.data.LogApplication
import com.example.monopoly.ui.viewmodel.ConfigActivityViewModel
import com.example.monopoly.ui.viewmodel.GameViewModel
import com.example.monopoly.ui.viewmodel.GameViewModelFactory
import com.example.monopoly.ui.viewmodel.LogViewModel
import com.example.monopoly.ui.viewmodel.LogViewModelFactory
import com.example.monopoly.ui.viewmodel.OtherGamesViewModel
import com.example.monopoly.ui.viewmodel.ResultsViewModel

enum class MenuScreens(@param:StringRes val title: Int) {
    Start(title = R.string.app_name),
    NewGame(title = R.string.NewGame),
    Config(title = R.string.Configuration),
    Help(title = R.string.Help),
    Results(title = R.string.TittleGameResults),
    OtherGames(title = R.string.OtherGames)
}


@Composable
fun AppNavigation() {
    val context = LocalContext.current
    val navController = rememberNavController()
    val application = context.applicationContext as Application

    // Factory for the db store
    val factory = ViewModelProvider.AndroidViewModelFactory.getInstance(application)
    val configViewModel: ConfigActivityViewModel = viewModel(factory = factory)

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
                onNavigateToOtherGames = { navController.navigate(MenuScreens.OtherGames.name) },
                onExit = {
                    (context as? Activity)?.finish()
                },
                modifier = Modifier
            )
        }

        // Config
        composable(MenuScreens.Config.name) {
            ConfigScreen(
                viewModel = configViewModel,
                onNavigateBack = { navController.popBackStack() },
            )
        }

        // Help
        composable(MenuScreens.Help.name) {
            HelpScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }

        // New Game
        composable(MenuScreens.NewGame.name) {
            val gameViewModel: GameViewModel = viewModel(
                factory = GameViewModelFactory(
                    application = application,
                    numPlayers = configViewModel.numPlayers,
                    playerNames = ArrayList(configViewModel.getSelectedPlayerNames()),
                    initialMinutes = configViewModel.getFinalTimeLimit(),
                    startMoney = configViewModel.startMoney.toIntOrNull() ?: 2000,
                    passGoMoney = configViewModel.passGoMoney.toIntOrNull() ?: 200,
                    jailTurns = configViewModel.jailTurns.toIntOrNull() ?: 3,
                    taxPrice = configViewModel.taxesPrice.toIntOrNull() ?: 200
                )
            )

            GameScreen(
                viewModel = gameViewModel,
                onExit = {
                    // Clean history and go back
                    navController.popBackStack(MenuScreens.Start.name, inclusive = false)
                },
                onResults = {
                    navController.navigate(MenuScreens.Results.name)
                },
            )
        }

        // Results
        composable(MenuScreens.Results.name) {
            val resultsViewModel: ResultsViewModel = viewModel()

            val app = context.applicationContext as LogApplication

            val logViewModel: LogViewModel = viewModel(
                factory = LogViewModelFactory(app.repository)
            )

            ResultsScreen(
                onNewGame = {
                    // Clean the stack and go back to start
                    navController.navigate(MenuScreens.NewGame.name) {
                        popUpTo(MenuScreens.Start.name) { inclusive = false }
                    }
                },
                logViewModel = logViewModel,
                onExit = { (context as? Activity)?.finish() },
                modifier = Modifier,
            )
        }

        // Get other games info
        composable(MenuScreens.OtherGames.name) {
            val otherGamesViewModel: OtherGamesViewModel = viewModel()

            OtherGamesScreen(
                modifier = Modifier,
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}
