package com.example.monopoly.ui.screens

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

import com.example.monopoly.ui.components.ActionsArea
import com.example.monopoly.ui.components.BoardArea
import com.example.monopoly.ui.components.HeaderArea
import com.example.monopoly.ui.theme.MonopolyTheme
import game.model.Board
import game.model.box.GameBox

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

    // Prepare data for components (Splitting board boxes)
    val boxesPerSide = board.size / 4
    val bottomBoxes = board.gameBoxes.subList(0, boxesPerSide + 1).reversed()
    val leftBoxes = board.gameBoxes.subList(boxesPerSide + 1, (boxesPerSide * 2))
    val topBoxes = board.gameBoxes.subList(boxesPerSide * 2, (boxesPerSide * 3) + 1)
    val rightBoxes = board.gameBoxes.subList((boxesPerSide * 3) + 1, board.size)

    // Manage UI State (Placeholder values for now)
    val currentPlayerMoney by remember { mutableIntStateOf(1500) }
    val ownedPropertyIcons = remember { mutableStateListOf<Int>() }


    GameContent(
        initialMinutes = initialMinutes,
        onExit = onExit,
        topGameBoxes = topBoxes,
        bottomGameBoxes = bottomBoxes,
        leftGameBoxes = leftBoxes,
        rightGameBoxes = rightBoxes,
        currentPlayerMoney = currentPlayerMoney,
        ownedPropertyIcons = ownedPropertyIcons,
        onBuyProperty = { /* TODO: Link to logic */ },
        onBuyHouse = { /* TODO: Link to logic */ },
        onNextTurn = { /* TODO: Link to logic */ },
        canBuyProperty = true,
        canBuyHouse = false,
        canNextTurn = true,
        modifier = modifier
    )
}

/**
 * Only responsible for layout and displaying data.
 */
@Composable
fun GameContent(
    initialMinutes: Int,
    onExit: () -> Unit,
    topGameBoxes: List<GameBox>,
    bottomGameBoxes: List<GameBox>,
    leftGameBoxes: List<GameBox>,
    rightGameBoxes: List<GameBox>,
    currentPlayerMoney: Int,
    ownedPropertyIcons: List<Int>,
    onBuyProperty: () -> Unit,
    onBuyHouse: () -> Unit,
    onNextTurn: () -> Unit,
    canBuyProperty: Boolean,
    canBuyHouse: Boolean,
    canNextTurn: Boolean,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxSize()) {
        // Top Section: Timer and Exit
        HeaderArea(
            initialMinutes = initialMinutes,
            onExitGame = onExit
        )

        // Middle Section: The Board
        Box(modifier = Modifier.weight(1f).padding(8.dp)) {
            BoardArea(
                topGameBoxes = topGameBoxes,
                bottomGameBoxes = bottomGameBoxes,
                leftGameBoxes = leftGameBoxes,
                rightGameBoxes = rightGameBoxes,
                centerContent = {
                    // Space reserved for dice and game status
                }
            )
        }

        // Bottom Section: Player stats and Actions
        ActionsArea(
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

@Preview(showBackground = true, widthDp = 400, heightDp = 800)
@Composable
fun GameScreenPreview() {
    MonopolyTheme {
        GameScreen(
            numPlayers = 2,
            playerNames = listOf("Player 1", "Player 2"),
            initialMinutes = 60,
            onExit = {}
        )
    }
}
