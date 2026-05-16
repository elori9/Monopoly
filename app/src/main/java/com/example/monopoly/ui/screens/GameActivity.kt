package com.example.monopoly.ui.screens

import android.content.Context
import android.content.res.Configuration
import android.media.SoundPool
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffold
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.layout.PaneAdaptedValue
import androidx.compose.material3.adaptive.layout.PaneScaffoldDirective
import androidx.compose.material3.adaptive.layout.calculatePaneScaffoldDirective
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import com.example.monopoly.R
import com.example.monopoly.ui.components.*
import com.example.monopoly.ui.components.animations.RollDice
import com.example.monopoly.ui.components.animations.WinnerAnimation
import com.example.monopoly.ui.theme.MonopolyTheme
import com.example.monopoly.ui.viewmodel.GameViewModel
import game.model.Player
import game.model.box.BoxName
import game.model.box.GameBox

/**
 * Stateful version of the Game Screen.
 * Manages the game state, board model and logic.
 */
@Composable
fun GameScreen(
    viewModel: GameViewModel,
    onExit: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    val currentOwnedIcons =
        remember(viewModel.currentPlayer, viewModel.currentPlayer?.properties?.size) {
            viewModel.currentPlayer?.properties?.map { property ->
                getCardIconByName(property.name)
            } ?: emptyList()
        }


    // Add sounds

    // Sound manager
    val soundPool = remember { SoundPool.Builder().build() }

    // Sounds
    val diceSoundId = remember { soundPool.load(context, R.raw.diceroll, 1) }
    val cashId = remember { soundPool.load(context, R.raw.cash, 1) }
    val wrong = remember { soundPool.load(context, R.raw.wrong, 1) }
    val win = remember { soundPool.load(context, R.raw.win, 1) }


    // Clean memory on exit as the mp3 are loaded on RAM since start of screen on sound pool
    DisposableEffect(Unit) {
        onDispose {
            soundPool.release()
        }
    }

    // Sounds trigger
    LaunchedEffect(viewModel.soundTrigger) {
        viewModel.soundTrigger?.let { sound ->
            when (sound) {
                Sounds.BUY -> soundPool.play(cashId, 1f, 1f, 0, 0, 1f)
                Sounds.DICE_ROLL -> soundPool.play(diceSoundId, 1f, 1f, 0, 0, 1f)
                Sounds.WRONG -> soundPool.play(wrong, 1f, 1f, 0, 0, 1f)
                Sounds.WIN -> soundPool.play(win, 1f, 1f, 0, 0, 1f)
            }
            // Restar the sound
            viewModel.restarSound()
        }
    }


    val isPortrait = LocalConfiguration.current.orientation == Configuration.ORIENTATION_PORTRAIT


    // Send the end message if is a winner
    LaunchedEffect(viewModel.goToGameResults) {
        if (viewModel.goToGameResults) {
            // Send log
            sendEndGameLog(context, viewModel.logBuilder)
        }
    }


    // Delegate to Stateless Content
    GameContent(
        isPortrait = isPortrait,
        initialMinutes = viewModel.initialMinutes,
        secondsRemaining = viewModel.secondsRemaining,
        onExit = onExit,
        topGameBoxes = viewModel.topBoxes,
        bottomGameBoxes = viewModel.bottomBoxes,
        leftGameBoxes = viewModel.leftBoxes,
        rightGameBoxes = viewModel.rightBoxes,
        allPlayers = viewModel.playersState,
        currentPlayerName = viewModel.currentPlayer?.name ?: "",
        currentPlayerId = viewModel.currentPlayer?.id ?: 0,
        currentPlayerMoney = viewModel.currentPlayerMoney,
        ownedPropertyIcons = currentOwnedIcons,
        gameMessage = viewModel.gameMessage,
        onBuyProperty = {
            viewModel.onBuyPropertyDecision(true)
        },
        onBuyHouse = {
            viewModel.onBuyHouseClicked()
        },
        onNextTurn = {
            viewModel.onNextTurnClicked()
        },
        onRollDice = {
            viewModel.onRollDiceClicked()
        },
        canBuyProperty = viewModel.canBuyProperty,
        canBuyHouse = viewModel.canBuyHouse,
        canNextTurn = viewModel.canNextTurn,
        currentDiceRoll = viewModel.dice,
        canRoll = viewModel.canRoll,
        logEntries = viewModel.logEntries,
        modifier = modifier
    )

    // Show Pop up if build house is selected
    if (viewModel.showBuildDialog)
        ShowPopUp(viewModel, context)

    // Show winn animation
    if (viewModel.showWinnerAnimation && viewModel.winner != null)
        WinnerAnimation(winner = viewModel.winner!!.name)
}

@Composable
fun ShowPopUp(viewModel: GameViewModel, context: Context) {
    Popup(
        alignment = Alignment.Center,
        // When you press out, so you can cancel the house build
        onDismissRequest = { viewModel.selectHouseToBuild(null) },
        properties = PopupProperties(focusable = true)
    ) {
        Surface(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(0.8f),
            shape = RoundedCornerShape(8.dp),
            shadowElevation = 8.dp
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(context.getString(R.string.SelectWhereBuild))

                // Houses
                viewModel.buildOptions.forEach { property ->
                    Button(onClick = { viewModel.selectHouseToBuild(property) }) {
                        Text("${property.name} - ${property.housePrice}€")
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Cancel button
                OutlinedButton(
                    onClick = { viewModel.selectHouseToBuild(null) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(context.getString(R.string.Cancel))
                }
            }
        }
    }
}

/**
 * Parse the icon name to a house resource ID
 */
fun getCardIconByName(name: String): Int {
    return when (name) {
        BoxName.LONELY.displayName -> R.drawable.lonely
        BoxName.TOMATO.displayName -> R.drawable.tomatotown
        BoxName.RETAIL.displayName -> R.drawable.retail
        BoxName.LOOT.displayName -> R.drawable.lootlake
        BoxName.SALTY.displayName -> R.drawable.salty
        BoxName.PARK.displayName -> R.drawable.park
        BoxName.GREASY.displayName -> R.drawable.greasy
        BoxName.TILTED.displayName -> R.drawable.tilted
        BoxName.FLUSH.displayName -> R.drawable.flush
        BoxName.DUSTY.displayName -> R.drawable.dusty
        BoxName.SNOBBY.displayName -> R.drawable.snobby
        BoxName.SHIFTY.displayName -> R.drawable.shifty
        BoxName.WAILING.displayName -> R.drawable.wailing
        BoxName.FATAL.displayName -> R.drawable.fatal
        BoxName.JUNK.displayName -> R.drawable.junk
        BoxName.HAUNTED.displayName -> R.drawable.haunted
        BoxName.MOISTY.displayName -> R.drawable.moisty
        BoxName.LUCKY_LANDING.displayName -> R.drawable.lucky

        else -> R.drawable.icon1
    }
}


/**
 * Stateless version of the Game Screen.
 * Handles layout and dispatches actions based on orientation.
 */
@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun GameContent(
    isPortrait: Boolean,
    initialMinutes: Int,
    secondsRemaining: Long,
    onExit: () -> Unit,
    topGameBoxes: List<GameBox>,
    bottomGameBoxes: List<GameBox>,
    leftGameBoxes: List<GameBox>,
    rightGameBoxes: List<GameBox>,
    allPlayers: List<Player>,
    currentPlayerName: String,
    currentPlayerId: Int,
    currentPlayerMoney: Int,
    ownedPropertyIcons: List<Int>,
    onBuyProperty: () -> Unit,
    onBuyHouse: () -> Unit,
    onNextTurn: () -> Unit,
    onRollDice: () -> Unit,
    gameMessage: String,
    canBuyProperty: Boolean,
    canBuyHouse: Boolean,
    canNextTurn: Boolean,
    currentDiceRoll: Int?,
    canRoll: Boolean,
    logEntries: List<String> = emptyList(),
    modifier: Modifier = Modifier
) {
    // Use ListDetailPaneScaffold for adaptive bi-panel layout
    // On tablets: detailPane (wider) = game, listPane (narrower) = log
    // On smartphones: only listPane is shown (mono-panel), so we put the game there too
    val baseDirective = calculatePaneScaffoldDirective(currentWindowAdaptiveInfo())
    val customDirective = PaneScaffoldDirective(
        maxHorizontalPartitions = baseDirective.maxHorizontalPartitions,
        horizontalPartitionSpacerSize = 0.dp,
        maxVerticalPartitions = baseDirective.maxVerticalPartitions,
        verticalPartitionSpacerSize = 0.dp,
        defaultPanePreferredWidth = 200.dp,
        excludedBounds = emptyList()
    )
    val navigator = rememberListDetailPaneScaffoldNavigator<Nothing>(
        scaffoldDirective = customDirective
    )

    // Check if the detail pane is visible (tablet bi-panel mode)
    val isDetailVisible = navigator.scaffoldValue[ListDetailPaneScaffoldRole.Detail] ==
        PaneAdaptedValue.Expanded

    ListDetailPaneScaffold(
        directive = navigator.scaffoldDirective,
        value = navigator.scaffoldValue,
        listPane = {
            // On tablets: listPane = log (narrower, ~1/4 of screen)
            // On smartphones: listPane = game (full screen, mono-panel)
            if (isDetailVisible) {
                // Tablet: show log in the narrower list pane
                GameLogPanel(
                    logEntries = logEntries,
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                // Smartphone: show the game (mono-panel, no log)
                GamePanel(
                    isPortrait = isPortrait,
                    initialMinutes = initialMinutes,
                    secondsRemaining = secondsRemaining,
                    onExit = onExit,
                    topGameBoxes = topGameBoxes,
                    bottomGameBoxes = bottomGameBoxes,
                    leftGameBoxes = leftGameBoxes,
                    rightGameBoxes = rightGameBoxes,
                    allPlayers = allPlayers,
                    currentPlayerName = currentPlayerName,
                    currentPlayerId = currentPlayerId,
                    currentPlayerMoney = currentPlayerMoney,
                    ownedPropertyIcons = ownedPropertyIcons,
                    gameMessage = gameMessage,
                    onRollDice = onRollDice,
                    onBuyProperty = onBuyProperty,
                    onBuyHouse = onBuyHouse,
                    onNextTurn = onNextTurn,
                    canBuyProperty = canBuyProperty,
                    canBuyHouse = canBuyHouse,
                    canNextTurn = canNextTurn,
                    currentDiceRoll = currentDiceRoll,
                    canRoll = canRoll,
                    modifier = modifier
                )
            }
        },
        detailPane = {
            // Tablet: detailPane = game (wider, ~3/4 of screen)
            GamePanel(
                isPortrait = isPortrait,
                initialMinutes = initialMinutes,
                secondsRemaining = secondsRemaining,
                onExit = onExit,
                topGameBoxes = topGameBoxes,
                bottomGameBoxes = bottomGameBoxes,
                leftGameBoxes = leftGameBoxes,
                rightGameBoxes = rightGameBoxes,
                allPlayers = allPlayers,
                currentPlayerName = currentPlayerName,
                currentPlayerId = currentPlayerId,
                currentPlayerMoney = currentPlayerMoney,
                ownedPropertyIcons = ownedPropertyIcons,
                gameMessage = gameMessage,
                onRollDice = onRollDice,
                onBuyProperty = onBuyProperty,
                onBuyHouse = onBuyHouse,
                onNextTurn = onNextTurn,
                canBuyProperty = canBuyProperty,
                canBuyHouse = canBuyHouse,
                canNextTurn = canNextTurn,
                currentDiceRoll = currentDiceRoll,
                canRoll = canRoll,
                modifier = modifier
            )
        },
        modifier = Modifier.fillMaxSize()
    )
}

/**
 * Helper composable that renders the game board in the appropriate orientation.
 */
@Composable
fun GamePanel(
    isPortrait: Boolean,
    initialMinutes: Int,
    secondsRemaining: Long,
    onExit: () -> Unit,
    topGameBoxes: List<GameBox>,
    bottomGameBoxes: List<GameBox>,
    leftGameBoxes: List<GameBox>,
    rightGameBoxes: List<GameBox>,
    allPlayers: List<Player>,
    currentPlayerName: String,
    currentPlayerId: Int,
    currentPlayerMoney: Int,
    ownedPropertyIcons: List<Int>,
    onBuyProperty: () -> Unit,
    onBuyHouse: () -> Unit,
    onNextTurn: () -> Unit,
    onRollDice: () -> Unit,
    gameMessage: String,
    canBuyProperty: Boolean,
    canBuyHouse: Boolean,
    canNextTurn: Boolean,
    currentDiceRoll: Int?,
    canRoll: Boolean,
    modifier: Modifier = Modifier
) {
    if (isPortrait) {
        DrawPortrait(
            initialMinutes = initialMinutes,
            secondsRemaining = secondsRemaining,
            onExit = onExit,
            topGameBoxes = topGameBoxes,
            bottomGameBoxes = bottomGameBoxes,
            leftGameBoxes = leftGameBoxes,
            rightGameBoxes = rightGameBoxes,
            allPlayers = allPlayers,
            currentPlayerName = currentPlayerName,
            currentPlayerId = currentPlayerId,
            currentPlayerMoney = currentPlayerMoney,
            ownedPropertyIcons = ownedPropertyIcons,
            gameMessage = gameMessage,
            onRollDice = onRollDice,
            onBuyProperty = onBuyProperty,
            onBuyHouse = onBuyHouse,
            onNextTurn = onNextTurn,
            canBuyProperty = canBuyProperty,
            canBuyHouse = canBuyHouse,
            canNextTurn = canNextTurn,
            currentDiceRoll = currentDiceRoll,
            canRoll = canRoll,
            modifier = modifier
        )
    } else {
        DrawLandscape(
            initialMinutes = initialMinutes,
            secondsRemaining = secondsRemaining,
            onExit = onExit,
            topGameBoxes = topGameBoxes,
            bottomGameBoxes = bottomGameBoxes,
            leftGameBoxes = leftGameBoxes,
            rightGameBoxes = rightGameBoxes,
            allPlayers = allPlayers,
            currentPlayerName = currentPlayerName,
            currentPlayerId = currentPlayerId,
            currentPlayerMoney = currentPlayerMoney,
            ownedPropertyIcons = ownedPropertyIcons,
            gameMessage = gameMessage,
            onRollDice = onRollDice,
            onBuyProperty = onBuyProperty,
            onBuyHouse = onBuyHouse,
            onNextTurn = onNextTurn,
            canBuyProperty = canBuyProperty,
            canBuyHouse = canBuyHouse,
            canNextTurn = canNextTurn,
            currentDiceRoll = currentDiceRoll,
            canRoll = canRoll,
            modifier = modifier
        )
    }
}

/**
 * Game Log Panel - Shows the progressive log of the game.
 * Displayed as the secondary (detail) pane on tablets.
 */
@Composable
fun GameLogPanel(
    logEntries: List<String>,
    modifier: Modifier = Modifier
) {
    val listState = rememberLazyListState()

    // Auto-scroll to the latest log entry
    LaunchedEffect(logEntries.size) {
        if (logEntries.isNotEmpty()) {
            listState.animateScrollToItem(logEntries.size - 1)
        }
    }

    Column(
        modifier = modifier
            .background(Color(0xFFF5F5F5))
            .padding(12.dp)
    ) {
        // Title
        Text(
            text = stringResource(R.string.GameLogTitle),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        HorizontalDivider(thickness = 1.dp, color = Color.LightGray)

        Spacer(modifier = Modifier.height(8.dp))

        // Log entries list
        LazyColumn(
            state = listState,
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            itemsIndexed(logEntries) { index, entry ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            if (index % 2 == 0) Color.White else Color(0xFFEEEEEE),
                            shape = RoundedCornerShape(4.dp)
                        )
                        .padding(horizontal = 8.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Index number
                    Text(
                        text = "${index + 1}.",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Gray,
                        modifier = Modifier.width(30.dp)
                    )
                    // Log message
                    Text(
                        text = entry,
                        fontSize = 13.sp,
                        color = Color.DarkGray
                    )
                }
            }
        }
    }
}

@Composable
fun DrawPortrait(
    initialMinutes: Int,
    secondsRemaining: Long,
    onExit: () -> Unit,
    topGameBoxes: List<GameBox>,
    bottomGameBoxes: List<GameBox>,
    leftGameBoxes: List<GameBox>,
    rightGameBoxes: List<GameBox>,
    allPlayers: List<Player>,
    currentPlayerName: String,
    currentPlayerId: Int,
    currentPlayerMoney: Int,
    ownedPropertyIcons: List<Int>,
    onBuyProperty: () -> Unit,
    onBuyHouse: () -> Unit,
    onNextTurn: () -> Unit,
    gameMessage: String,
    onRollDice: () -> Unit,
    canBuyProperty: Boolean,
    canBuyHouse: Boolean,
    canNextTurn: Boolean,
    currentDiceRoll: Int?,
    canRoll: Boolean,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxSize()) {
        // Top Section: Timer, Turn Info and Exit
        GameHeaderAreaPortrait(
            secondsRemaining = secondsRemaining,
            isTimerEnabled = initialMinutes > 0,
            currentPlayerName = currentPlayerName,
            currentPlayerId = currentPlayerId,
            onExitGame = onExit
        )

        // Middle Section: The Board
        Box(
            modifier = Modifier
                .weight(1f)
                .padding(8.dp)
        ) {
            BoardArea(
                topGameBoxes = topGameBoxes,
                bottomGameBoxes = bottomGameBoxes,
                leftGameBoxes = leftGameBoxes,
                rightGameBoxes = rightGameBoxes,
                allPlayers = allPlayers,
                gameMessage = gameMessage,
                centerContent = {
                    val displayResult = currentDiceRoll ?: 1 // Shouldn't fail and get 1
                    RollDice(
                        result = displayResult,
                        onRollClick = onRollDice,
                        enabled = canRoll
                    )
                }
            )
        }

        // Bottom Section: Player stats and Actions
        ActionsAreaPortrait(
            currentPlayerMoney = currentPlayerMoney,
            ownedPropertyIcons = ownedPropertyIcons,
            onBuyProperty = onBuyProperty,
            onBuyHouse = onBuyHouse,
            onNextTurn = onNextTurn,
            canBuyProperty = canBuyProperty,
            canBuyHouse = canBuyHouse,
            canNextTurn = canNextTurn
        )
    }
}

@Composable
fun DrawLandscape(
    initialMinutes: Int,
    secondsRemaining: Long,
    onExit: () -> Unit,
    topGameBoxes: List<GameBox>,
    bottomGameBoxes: List<GameBox>,
    leftGameBoxes: List<GameBox>,
    rightGameBoxes: List<GameBox>,
    allPlayers: List<Player>,
    currentPlayerName: String,
    currentPlayerId: Int,
    currentPlayerMoney: Int,
    ownedPropertyIcons: List<Int>,
    onBuyProperty: () -> Unit,
    onBuyHouse: () -> Unit,
    onNextTurn: () -> Unit,
    gameMessage: String,
    onRollDice: () -> Unit,
    canBuyProperty: Boolean,
    canBuyHouse: Boolean,
    canNextTurn: Boolean,
    currentDiceRoll: Int?,
    canRoll: Boolean,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxSize()
            .padding(6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Left Column: Owned Properties
        Column(
            modifier = Modifier
                .weight(0.18f)
                .padding(end = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            SmartPropertiesAreaVertical(
                ownedPropertyIcons = ownedPropertyIcons,
                modifier = Modifier.fillMaxHeight()
            )
        }

        // Middle Box: Board
        Box(
            modifier = Modifier
                .weight(0.52f)
                .fillMaxHeight()
                .padding(horizontal = 8.dp),
            contentAlignment = Alignment.Center
        ) {
            BoardArea(
                topGameBoxes = topGameBoxes,
                bottomGameBoxes = bottomGameBoxes,
                leftGameBoxes = leftGameBoxes,
                rightGameBoxes = rightGameBoxes,
                allPlayers = allPlayers,
                gameMessage = gameMessage,
                centerContent = {
                    val displayResult = currentDiceRoll ?: 1 // Shouldn't fail and get 1
                    RollDice(
                        result = displayResult,
                        onRollClick = onRollDice,
                        enabled = canRoll
                    )
                }
            )
        }

        // Right Column: Info and Actions
        Column(
            modifier = Modifier
                .weight(0.3f)
                .fillMaxHeight()
                .padding(start = 6.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.1f),
                horizontalArrangement = Arrangement.End
            ) {
                SmartHeaderButtons(onExitGame = onExit)
            }

            // Turn Info in Landscape
            TurnInfo(
                playerName = currentPlayerName,
                playerIconRes = getTokenByPlayerId(currentPlayerId),
                modifier = Modifier
                    .weight(0.2f)
                    .padding(vertical = 4.dp)
            )

            // Timer
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.15f),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                GameTimer(secondsRemaining = secondsRemaining, isTimerEnabled = initialMinutes > 0)
            }

            // Action area
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.55f)
                    .padding(start = 16.dp),
            ) {
                ShowPlayerActions(
                    currentPlayerMoney = currentPlayerMoney,
                    onBuyProperty = onBuyProperty,
                    onBuyHouse = onBuyHouse,
                    onNextTurn = onNextTurn,
                    canBuyProperty = canBuyProperty,
                    canBuyHouse = canBuyHouse,
                    canNextTurn = canNextTurn,
                    modifier = Modifier
                )
            }
        }
    }
}


private fun sendEndGameLog(context: Context, info: String) {
    // TODO SAVE ON MEMORY
}

@Preview(showBackground = true, widthDp = 800, heightDp = 400)
@Composable
fun GameScreenLandscapePreview() {
    MonopolyTheme {

        GameContent(
            isPortrait = false,
            initialMinutes = 60,
            secondsRemaining = 3600L,
            onExit = {},

            topGameBoxes = emptyList(),
            bottomGameBoxes = emptyList(),
            leftGameBoxes = emptyList(),
            rightGameBoxes = emptyList(),

            allPlayers = listOf(
                Player(id = 0, name = "p1", money = 1500),
                Player(id = 1, name = "p2", money = 2000)
            ),
            currentPlayerName = "p1",
            currentPlayerId = 0,
            currentPlayerMoney = 1500,

            ownedPropertyIcons = listOf(R.drawable.tomatotown, R.drawable.park),

            gameMessage = "msg",
            onBuyProperty = {},
            onBuyHouse = {},
            onNextTurn = {},
            onRollDice = {},

            canBuyProperty = false,
            canBuyHouse = true,
            canNextTurn = false,
            canRoll = true,
            currentDiceRoll = 4
        )
    }
}
