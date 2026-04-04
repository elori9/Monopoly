package com.example.monopoly.ui.screens

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.monopoly.R

import com.example.monopoly.ui.components.ActionsAreaPortrait
import com.example.monopoly.ui.components.BoardArea
import com.example.monopoly.ui.components.GameTimer
import com.example.monopoly.ui.components.HeaderAreaPortrait
import com.example.monopoly.ui.components.ShowPlayerActions
import com.example.monopoly.ui.components.SmartHeaderButtons
import com.example.monopoly.ui.components.SmartPropertiesArea
import com.example.monopoly.ui.components.animations.RollDice
import com.example.monopoly.ui.theme.MonopolyTheme
import com.example.monopoly.ui.viewmodel.GameViewModel
import game.controller.GameController
import game.model.Board
import game.model.Dice
import game.model.Player
import game.model.TurnAction
import game.model.box.BoxName
import game.model.box.GameBox
import kotlinx.coroutines.delay
import kotlin.collections.emptyList

class GameActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val numPlayers = intent?.getIntExtra("NUM_PLAYERS", 2) ?: 2
        val playerNames = intent?.getStringArrayListExtra("PLAYER_NAMES") ?: arrayListOf()
        val timerMinutes = intent?.getIntExtra("TIME_LIMIT", 0) ?: 0

        setContent {
            MonopolyTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    GameScreen(
                        numPlayers = numPlayers,
                        playerNames = playerNames,
                        initialMinutes = timerMinutes,
                        onExit = {
                            val intent = Intent(this, MainActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                            startActivity(intent)
                            finish()
                        },
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

/**
 * Manages the game state, board model and logic.
 */
@Composable
fun GameScreen(
    numPlayers: Int,
    playerNames: List<String>,
    initialMinutes: Int,
    onExit: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Manage Model State

    val board = remember {
        Board().apply { generateBoard(numPlayers) }
    }

    val viewModel = remember { GameViewModel() }

    val controller = remember {
        // Get the players
        val players =
            playerNames.mapIndexed { index, name -> Player(id = index, name = name, money = 2000) }

        viewModel.playersState.addAll(players)

        GameController(
            view = viewModel,
            board = board,
            players = players,
            timeLimit = initialMinutes,
            dice = Dice()
        )
    }

    LaunchedEffect(Unit) {
        controller.startGame()
    }

    // Prepare data for components (Splitting board boxes)
    val boxesPerSide = board.size / 4
    val bottomBoxes = board.gameBoxes.subList(0, boxesPerSide + 1).reversed()
    val leftBoxes = board.gameBoxes.subList(boxesPerSide + 1, (boxesPerSide * 2))
    val topBoxes = board.gameBoxes.subList(boxesPerSide * 2, (boxesPerSide * 3) + 1)
    val rightBoxes = board.gameBoxes.subList((boxesPerSide * 3) + 1, board.size)

    // Owned houses
    val currentOwnedIcons =
        remember(viewModel.currentPlayer, viewModel.currentPlayer?.properties?.size) {
            // Recalculate the other player cards or the new properties for the same player
            viewModel.currentPlayer?.properties?.map { property ->
                getCardIconByName(property.name)
            } ?: emptyList()
        }

    // Timer
    var secondsRemaining by rememberSaveable(initialMinutes) {
        mutableLongStateOf(initialMinutes.toLong() * 60L)
    }

    if (initialMinutes > 0) {
        LaunchedEffect(Unit) {
            while (secondsRemaining > 0) {
                delay(1000L)
                secondsRemaining--
            }
        }
    }

    // Orientation
    val isPortrait = LocalConfiguration.current.orientation == Configuration.ORIENTATION_PORTRAIT

    GameContent(
        isPortrait = isPortrait,
        initialMinutes = initialMinutes,
        secondsRemaining = secondsRemaining,
        onExit = onExit,
        topGameBoxes = topBoxes,
        bottomGameBoxes = bottomBoxes,
        leftGameBoxes = leftBoxes,
        rightGameBoxes = rightBoxes,
        allPlayers = viewModel.playersState,
        currentPlayerMoney = viewModel.currentPlayerMoney,
        ownedPropertyIcons = currentOwnedIcons,
        gameMessage = viewModel.gameMessage,
        onBuyProperty = {
            // Save action
            val action = viewModel.buyProperty
            // Clean before use
            viewModel.buyProperty = null
            // Execute
            action?.invoke(true)
        },
        onBuyHouse = {
            // Save action
            val action = viewModel.turnAction
            // Clean before use
            viewModel.turnAction = null
            // Execute
            action?.invoke(TurnAction.BUILD_HOUSE)
        },
        onNextTurn = {
            if (viewModel.buyProperty != null) {
                // Don't buy
                val action = viewModel.buyProperty
                viewModel.buyProperty = null
                action?.invoke(false)
            } else {
                // roll
                val action = viewModel.turnAction
                viewModel.turnAction = null
                action?.invoke(TurnAction.ROLL_DICE)
            }
        },
        // If there is smt null means you can't do the option so ->
        canBuyProperty = viewModel.buyProperty != null,
        canBuyHouse = viewModel.turnAction != null,
        canNextTurn = viewModel.turnAction != null || viewModel.buyProperty != null,
        currentDiceRoll = viewModel.dice,
        modifier = modifier
    )
}

/**
 * Parse the icon name to a house list
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

        else -> R.drawable.icon1 // Shouldn't happen
    }
}

/**
 * Only responsible for layout and displaying data.
 */
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
    currentPlayerMoney: Int,
    ownedPropertyIcons: List<Int>,
    onBuyProperty: () -> Unit,
    onBuyHouse: () -> Unit,
    onNextTurn: () -> Unit,
    gameMessage: String,
    canBuyProperty: Boolean,
    canBuyHouse: Boolean,
    canNextTurn: Boolean,
    currentDiceRoll: Int?,
    modifier: Modifier = Modifier
) {
    // Draw depending on orientation
    if (
        isPortrait
    ) {
        DrawPortrait(
            initialMinutes = initialMinutes,
            secondsRemaining = secondsRemaining,
            onExit = onExit,
            topGameBoxes = topGameBoxes,
            bottomGameBoxes = bottomGameBoxes,
            leftGameBoxes = leftGameBoxes,
            rightGameBoxes = rightGameBoxes,
            allPlayers = allPlayers,
            currentPlayerMoney = currentPlayerMoney,
            ownedPropertyIcons = ownedPropertyIcons,
            gameMessage = gameMessage,
            onBuyProperty = onBuyProperty,
            onBuyHouse = onBuyHouse,
            onNextTurn = onNextTurn,
            canBuyProperty = canBuyProperty,
            canBuyHouse = canBuyHouse,
            canNextTurn = canNextTurn,
            currentDiceRoll = currentDiceRoll,
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
            currentPlayerMoney = currentPlayerMoney,
            ownedPropertyIcons = ownedPropertyIcons,
            gameMessage = gameMessage,
            onBuyProperty = onBuyProperty,
            onBuyHouse = onBuyHouse,
            onNextTurn = onNextTurn,
            canBuyProperty = canBuyProperty,
            canBuyHouse = canBuyHouse,
            canNextTurn = canNextTurn,
            currentDiceRoll = currentDiceRoll,
            modifier = modifier
        )
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
    currentPlayerMoney: Int,
    ownedPropertyIcons: List<Int>,
    onBuyProperty: () -> Unit,
    onBuyHouse: () -> Unit,
    onNextTurn: () -> Unit,
    gameMessage: String,
    canBuyProperty: Boolean,
    canBuyHouse: Boolean,
    canNextTurn: Boolean,
    currentDiceRoll: Int?,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxSize()) {
        // Top Section: Timer and Exit
        HeaderAreaPortrait(
            secondsRemaining = secondsRemaining,
            isTimerEnabled = initialMinutes > 0, // the timer will be on if there is time
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
                    // Dice
                    if (currentDiceRoll != null) {
                        RollDice(
                            result = currentDiceRoll,
                            onRollClick = { onNextTurn() }
                        )
                    }
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
    currentPlayerMoney: Int,
    ownedPropertyIcons: List<Int>,
    onBuyProperty: () -> Unit,
    onBuyHouse: () -> Unit,
    onNextTurn: () -> Unit,
    gameMessage: String,
    canBuyProperty: Boolean,
    canBuyHouse: Boolean,
    canNextTurn: Boolean,
    currentDiceRoll: Int?,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxSize()
            .padding(6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Left
        Column(
            modifier = Modifier
                .weight(0.15f)
                .padding(end = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            SmartPropertiesArea(
                ownedPropertyIcons = ownedPropertyIcons,
            )
        }

        // Mid
        Box(
            modifier = Modifier
                .weight(0.55f)
                .fillMaxHeight()
                .padding(horizontal = 16.dp),
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
                    // Dice
                    if (currentDiceRoll != null) {
                        RollDice(
                            result = currentDiceRoll,
                            onRollClick = { onNextTurn() }
                        )
                    }
                }
            )
        }

        // Right
        Column(
            modifier = Modifier
                .weight(0.3f)
                .fillMaxHeight()
                .padding(start = 6.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            // Header icons
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.1f),
                horizontalArrangement = Arrangement.End
            ) {
                SmartHeaderButtons(onExitGame = onExit)
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Timer
            Row(
                modifier = modifier
                    .fillMaxSize()
                    .weight(0.25f)
                    .fillMaxHeight(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                GameTimer(secondsRemaining = secondsRemaining, isTimerEnabled = initialMinutes > 0)
            }

            // Action area
            Row(
                modifier = modifier
                    .fillMaxSize()
                    .weight(0.75f)
                    .fillMaxHeight()
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

@Preview(showBackground = true, widthDp = 600, heightDp = 250)
@Composable
fun GameScreenLandscapePreview() {
    MonopolyTheme {
        GameScreen(
            numPlayers = 2,
            playerNames = listOf("Player 1", "Player 2"),
            initialMinutes = 60,
            onExit = {}
        )
    }
}
