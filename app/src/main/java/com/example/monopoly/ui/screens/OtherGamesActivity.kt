package com.example.monopoly.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffold
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.layout.PaneAdaptedValue
import androidx.compose.material3.adaptive.layout.PaneScaffoldDirective
import androidx.compose.material3.adaptive.layout.calculatePaneScaffoldDirective
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.window.core.layout.WindowWidthSizeClass
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.monopoly.R
import com.example.monopoly.data.LogApplication
import com.example.monopoly.data.LogEntity
import com.example.monopoly.ui.components.ScreensHeaderArea
import com.example.monopoly.ui.viewmodel.LogViewModel
import com.example.monopoly.ui.viewmodel.LogViewModelFactory
import kotlinx.coroutines.launch


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

/**
 * Stateless content composable that uses ListDetailPaneScaffold.
 * - Tablet: bi-panel (list of games + detail of selected game)
 * - Smartphone: mono-panel with navigation between list and detail
 */
@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun OtherGamesContent(
    modifier: Modifier,
    onNavigateBack: () -> Unit,
    logs: List<LogEntity>
) {
    val adaptiveInfo = currentWindowAdaptiveInfo()
    val baseDirective = calculatePaneScaffoldDirective(adaptiveInfo)
    val isTablet = adaptiveInfo.windowSizeClass.windowWidthSizeClass != WindowWidthSizeClass.COMPACT

    val customDirective = PaneScaffoldDirective(
        maxHorizontalPartitions = if (isTablet) 2 else 1,
        horizontalPartitionSpacerSize = 0.dp,
        maxVerticalPartitions = baseDirective.maxVerticalPartitions,
        verticalPartitionSpacerSize = 0.dp,
        defaultPanePreferredWidth = 360.dp,
        excludedBounds = emptyList()
    )
    val navigator = rememberListDetailPaneScaffoldNavigator<Int>(
        scaffoldDirective = customDirective
    )
    val coroutineScope = rememberCoroutineScope()

    BackHandler(enabled = navigator.canNavigateBack()) {
        coroutineScope.launch {
            navigator.navigateBack()
        }
    }

    ListDetailPaneScaffold(
        directive = navigator.scaffoldDirective,
        value = navigator.scaffoldValue,
        listPane = {
            // AccessBD composable — list of all games
            AnimatedPane {
                AccessBD(
                    logs = logs,
                    onNavigateBack = onNavigateBack,
                    onGameSelected = { logId ->
                        coroutineScope.launch {
                            navigator.navigateTo(
                                ListDetailPaneScaffoldRole.Detail,
                                logId
                            )
                        }
                    },
                    modifier = modifier
                )
            }
        },
        detailPane = {
            // DetailReg composable — detail of the selected game
            AnimatedPane {
                val selectedLogId = navigator.currentDestination?.content
                val selectedLog = logs.find { it.id == selectedLogId }
                DetailReg(
                    log = selectedLog,
                    showBackButton = !isTablet,
                    onNavigateBack = {
                        coroutineScope.launch { navigator.navigateBack() }
                    },
                    modifier = modifier
                )
            }
        },
        modifier = Modifier.fillMaxSize()
    )
}

/**
 * List pane: shows all saved games in a LazyColumn.
 * Each item displays: alias, date, result (winner).
 * Clicking an item navigates to the detail pane.
 */
@Composable
fun AccessBD(
    logs: List<LogEntity>,
    onNavigateBack: () -> Unit,
    onGameSelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Header
        item {
            ScreensHeaderArea(
                onExit = onNavigateBack,
                modifier = Modifier,
                title = stringResource(id = R.string.OtherGames)
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        // Game items
        items(logs) { log ->
            GameListItem(
                log = log,
                onClick = { onGameSelected(log.id) }
            )
        }
    }
}

/**
 * A single row in the games list showing summary info.
 */
@Composable
fun GameListItem(
    log: LogEntity,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .background(Color(0xFFE3F2FD), shape = RoundedCornerShape(8.dp))
            .padding(12.dp)
    ) {
        // Winner name (alias)
        Text(
            text = log.winnerName,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            color = Color.Black
        )
        // Date
        Text(
            text = log.date,
            fontSize = 13.sp,
            color = Color.Gray
        )
        // Result summary
        Text(
            text = stringResource(R.string.GameDetailTurns, log.totalTurns) + " · " +
                    stringResource(R.string.GameDetailDuration, log.durationMinutes),
            fontSize = 12.sp,
            color = Color.DarkGray
        )
    }
    Spacer(modifier = Modifier.height(8.dp))
}

/**
 * Detail pane: shows full details of the selected game.
 * Shown in the detailPane on tablets, or navigated to on smartphones.
 */
@Composable
fun DetailReg(
    log: LogEntity?,
    showBackButton: Boolean = false,
    onNavigateBack: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        // Title
        Text(
            text = stringResource(R.string.GameDetailTitle),
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(8.dp))
        HorizontalDivider(thickness = 1.dp, color = Color.LightGray)
        Spacer(modifier = Modifier.height(16.dp))

        if (log != null) {
            // Winner
            Text(
                text = stringResource(R.string.GameDetailWinner, log.winnerName),
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(8.dp))

            // Date
            Text(
                text = stringResource(R.string.GameDetailDate, log.date),
                fontSize = 15.sp,
                color = Color.DarkGray
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Turns
            Text(
                text = stringResource(R.string.GameDetailTurns, log.totalTurns),
                fontSize = 15.sp,
                color = Color.DarkGray
            )
            Spacer(modifier = Modifier.height(4.dp))

            // Duration
            Text(
                text = stringResource(R.string.GameDetailDuration, log.durationMinutes),
                fontSize = 15.sp,
                color = Color.DarkGray
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Logs
            Text(
                text = stringResource(R.string.GameLogTitle),
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(8.dp))

            androidx.compose.foundation.layout.Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .background(Color(0xFFF5F5F5), RoundedCornerShape(8.dp))
                    .padding(8.dp)
            ) {
                Text(
                    text = log.logLine,
                    fontSize = 14.sp,
                    color = Color.DarkGray,
                    modifier = Modifier.verticalScroll(androidx.compose.foundation.rememberScrollState())
                )
            }
        } else {
            // No game selected placeholder
            Text(
                text = stringResource(R.string.SelectGamePrompt),
                fontStyle = FontStyle.Italic,
                fontSize = 16.sp,
                color = Color.Gray
            )
            Spacer(modifier = Modifier.weight(1f))
        }

        if (showBackButton) {
            Button(
                onClick = onNavigateBack,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            ) {
                Text(text = stringResource(R.string.ViewListAction))
            }
        }
    }
}


@Preview(showBackground = true, heightDp = 900, widthDp = 550, name = "Smartphone Portrait")
@Composable
fun OtherGamesPreview() {
    val dummyLogs = listOf(
        LogEntity(
            id = 1,
            date = "17/05/2026",
            winnerName = "Jugador 1",
            totalTurns = 20,
            durationMinutes = 45,
            logLine = "LOG"
        ),
        LogEntity(
            id = 2,
            date = "18/05/2026",
            winnerName = "Jugador 2",
            totalTurns = 35,
            durationMinutes = 60,
            logLine = "LOG"
        )
    )

    OtherGamesContent(
        modifier = Modifier,
        onNavigateBack = {},
        logs = dummyLogs,
    )
}

@Preview(showBackground = true, heightDp = 800, widthDp = 1280, name = "Tablet Landscape")
@Composable
fun OtherGamesTabletPreview() {
    val dummyLogs = listOf(
        LogEntity(
            id = 1,
            date = "17/05/2026",
            winnerName = "Jugador 1",
            totalTurns = 20,
            durationMinutes = 45,
            logLine = "LOG"
        ),
        LogEntity(
            id = 2,
            date = "18/05/2026",
            winnerName = "Jugador 2",
            totalTurns = 35,
            durationMinutes = 60,
            logLine = "LOG"
        )
    )

    OtherGamesContent(
        modifier = Modifier,
        onNavigateBack = {},
        logs = dummyLogs,
    )
}