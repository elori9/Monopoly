package com.example.monopoly.ui.screens

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.media.SoundPool
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import com.example.monopoly.R
import com.example.monopoly.ui.components.*
import com.example.monopoly.ui.components.animations.RollDice
import com.example.monopoly.ui.components.animations.WinnerAnimation
import com.example.monopoly.ui.theme.MonopolyTheme
import com.example.monopoly.ui.viewmodel.GameViewModel
import com.example.monopoly.ui.viewmodel.GameViewModelFactory
import game.model.Player
import game.model.box.BoxName
import game.model.box.GameBox
import kotlinx.coroutines.delay

class GameActivity : ComponentActivity() {
    val viewModel: GameViewModel by viewModels {
        // Get the intent there for pass to the view model
        val numPlayers = intent?.getIntExtra("NUM_PLAYERS", 2) ?: 2
        val playerNames = intent?.getStringArrayListExtra("PLAYER_NAMES") ?: arrayListOf()
        val timerMinutes = intent?.getIntExtra("TIME_LIMIT", 0) ?: 0
        val startMoney = intent?.getIntExtra("STARTING_MONEY", 2000) ?: 2000
        val passGoMoney = intent?.getIntExtra("PASS_GO_MONEY", 200) ?: 200
        val jailTurns = intent?.getIntExtra("JAIL_TURNS", 3) ?: 3
        val taxPrice = intent?.getIntExtra("TAX_PRICE", 200) ?: 200

        GameViewModelFactory(
            application = application,
            numPlayers = numPlayers,
            playerNames = playerNames,
            initialMinutes = timerMinutes,
            startMoney = startMoney,
            passGoMoney = passGoMoney,
            jailTurns = jailTurns,
            taxPrice = taxPrice
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            MonopolyTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    GameScreen(
                        viewModel = viewModel,
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
        HeaderAreaPortrait(
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
    val intent = Intent(context, Results::class.java).apply {
        putExtra("INFO", info)
    }
    context.startActivity(intent)

    // Close this activity (so first we need to cast the context for do it)
    (context as? ComponentActivity)?.finish()
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
