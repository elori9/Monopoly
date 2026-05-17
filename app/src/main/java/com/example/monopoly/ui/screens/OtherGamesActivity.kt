package com.example.monopoly.ui.screens

import android.content.res.Configuration
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.monopoly.R
import com.example.monopoly.data.LogApplication
import com.example.monopoly.data.LogEntity
import com.example.monopoly.ui.components.ScreensHeaderArea
import com.example.monopoly.ui.viewmodel.LogViewModel
import com.example.monopoly.ui.viewmodel.LogViewModelFactory
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text


@Composable
fun OtherGamesScreen(
    modifier: Modifier,
    onNavigateBack: () -> Unit,
) {
    val context = LocalContext.current
    val application = context.applicationContext
    val logViewModel: LogViewModel = viewModel(
        factory = LogViewModelFactory(
            (application as LogApplication).repository
        )
    )
    val logs by logViewModel.allLogs.collectAsStateWithLifecycle(initialValue = emptyList())

    OtherGamesContent(
        modifier = modifier,
        onNavigateBack = onNavigateBack,
        logs = logs
    )
}

@Composable
fun OtherGamesContent(
    modifier: Modifier,
    onNavigateBack: () -> Unit,
    logs: List<LogEntity>
) {
    val isPortrait = LocalConfiguration.current.orientation == Configuration.ORIENTATION_PORTRAIT

    if (isPortrait) {
        DrawOtherGameScreenPortrait(
            onNavigateBack = onNavigateBack,
            modifier = Modifier,
            logs = logs
        )
    } else {
        DrawOtherGameScreenLandscape(
            onNavigateBack = onNavigateBack,
            modifier = Modifier,
            logs = logs
        )
    }
}

@Composable
fun DrawOtherGameScreenPortrait(
    onNavigateBack: () -> Unit,
    modifier: Modifier,
    logs: List<LogEntity>
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        //Header
        item {
            ScreensHeaderArea(
                onExit = onNavigateBack,
                modifier = Modifier,
                title = stringResource(id = R.string.OtherGames)
            )
            Spacer(modifier = Modifier.height(24.dp))
        }

        // Content
        items(logs) { log ->
            Text(
                text = "Log: ${log.toString()}",
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }
    }
}

@Composable
fun DrawOtherGameScreenLandscape(
    onNavigateBack: () -> Unit,
    modifier: Modifier,
    logs: List<LogEntity>
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        //Header
        item {
            ScreensHeaderArea(
                onExit = onNavigateBack,
                modifier = Modifier,
                title = stringResource(id = R.string.OtherGames)
            )
            Spacer(modifier = Modifier.height(24.dp))
        }

        // Content
        items(logs) { log ->
            Text(
                text = "$log",
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }
    }
}

@Preview(showBackground = true, heightDp = 900, widthDp = 550)
@Composable
fun OtherGamesPreview() {
    val dummyLogs = listOf(
        LogEntity(
            id = 1,
            date = "17/05/2026",
            winnerName = "Jugador 1",
            totalTurns = 20,
            durationMinutes = 45,
        ),
        LogEntity(
            id = 2,
            date = "18/05/2026",
            winnerName = "Jugador 2",
            totalTurns = 35,
            durationMinutes = 60,
        )
    )

    OtherGamesContent(
        modifier = Modifier,
        onNavigateBack = {},
        logs = dummyLogs,
    )
}