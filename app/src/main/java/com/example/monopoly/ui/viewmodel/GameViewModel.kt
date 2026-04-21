package com.example.monopoly.ui.viewmodel


import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.monopoly.R
import game.controller.GameController
import game.interfaces.GameView
import game.model.Board
import game.model.Player
import game.model.TurnAction
import game.model.box.Property
import game.model.Dice
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class GameViewModel(
    application: Application,
    private val numPlayers: Int,
    private val playerNames: List<String>,
    val initialMinutes: Int,
    private val startMoney: Int,
    private val passGoMoney: Int,
    private val jailTurns: Int,
    private val taxPrice: Int
) : GameView, AndroidViewModel(application) {

    // State vars
    val playersState = mutableStateListOf<Player>()
    var currentPlayerMoney by mutableIntStateOf(0)
    var gameMessage by mutableStateOf("")
    var dice by mutableStateOf<Int?>(null)
    var currentPlayer by mutableStateOf<Player?>(null)
    var winner by mutableStateOf<Player?>(null)
        private set
    var logBuilder by mutableStateOf("")
        private set
    var secondsRemaining by mutableLongStateOf(initialMinutes.toLong() * 60L)
        private set


    // Board calculations
    private val board =
        Board().apply {
            generateBoard(numPlayers, passGoMoney, jailTurns, taxPrice)
        }
    private val boxesPerSide = board.size / 4
    val bottomBoxes = board.gameBoxes.subList(0, boxesPerSide + 1).reversed()
    val leftBoxes = board.gameBoxes.subList(boxesPerSide + 1, (boxesPerSide * 2))
    val topBoxes = board.gameBoxes.subList(boxesPerSide * 2, (boxesPerSide * 3) + 1)
    val rightBoxes = board.gameBoxes.subList((boxesPerSide * 3) + 1, board.size)

    // Those will be the callbacks
    var turnAction: ((TurnAction) -> Unit)? by mutableStateOf(null)
    var buyProperty: ((Boolean) -> Unit)? by mutableStateOf(null)
    var endTurnAction: (() -> Unit)? by mutableStateOf(null)


    // Flags for buttons activation
    val canRoll get() = turnAction != null
    val canBuyProperty get() = buyProperty != null
    val canNextTurn get() = endTurnAction != null
    val canBuyHouse get() = turnAction != null


    // Controller
    private val controller: GameController

    // Initialization for the game
    init {
        // Add the players
        val players = playerNames.mapIndexed { index, name ->
            Player(id = index, name = name, money = startMoney)
        }
        playersState.addAll(players)

        // Start the log
        val context = getApplication<Application>()
        addLog(context.getString(R.string.LogTittle))
        addLog("${context.getString(R.string.LogNumPlayers)} $numPlayers")
        addLog("${context.getString(R.string.LogNamePlayers)} ${playerNames.joinToString(", ")}")
        addLog(
            if (initialMinutes > 0) "${context.getString(R.string.LogTimerOn)} $initialMinutes"
            else context.getString(R.string.LogTimeOff)
        )

        // Start the controller
        controller = GameController(
            view = this,
            board = Board().apply { generateBoard(numPlayers, passGoMoney, jailTurns, taxPrice) },
            players = players,
            timeLimit = initialMinutes,
            dice = Dice()
        )
        // Start the game
        controller.startGame()

        // Time logic
        if (initialMinutes > 0 && winner == null) {
            // Use scope launch to start the timer as need to be on another thread
            viewModelScope.launch {
                while (secondsRemaining > 0) {
                    delay(1000L)
                    secondsRemaining--
                }
            }

            // Time over
            if (secondsRemaining == 0L && winner == null) {
                addLog(context.getString(R.string.LogTimerOn))
                controller.endGame()
            }
        }
    }


    // Click events
    fun onRollDiceClicked() {
        val callback = turnAction
        turnAction = null
        callback?.invoke(TurnAction.ROLL_DICE)
    }
    fun onBuyHouseClicked() {
        val callback = turnAction
        turnAction = null
        callback?.invoke(TurnAction.BUILD_HOUSE)
    }
    fun onBuyPropertyDecision(buy: Boolean) {
        val callback = buyProperty
        buyProperty = null
        callback?.invoke(buy)
    }
    fun onNextTurnClicked() {
        val callback = endTurnAction
        endTurnAction = null
        callback?.invoke()
    }


    // Implementation of the interface

    override fun showMessage(message: String) {
        gameMessage = message
    }

    override fun showTurnOptions(
        player: Player,
        onActionSelected: (TurnAction) -> Unit
    ) {
        currentPlayer = player
        currentPlayerMoney = player.money
        turnAction = onActionSelected
    }

    override fun showDiceRoll(playerName: String, roll: Int) {
        val context = getApplication<Application>()
        gameMessage = context.getString(R.string.ShowDiceRollMessage, playerName, roll)
        dice = roll
    }

    override fun updatePlayerPosition(playerId: Int, newPosition: Int) {
        // Compose will redraw the player, who will be on new position, no use of newPosition, player has it as attribute
        triggerPlayerRecomposition(playerId)
    }

    override fun updatePlayerMoney(playerId: Int, money: Int) {
        // Compose will redraw the player, who will update the money, there save the update
        currentPlayerMoney = money
        triggerPlayerRecomposition(playerId)
    }

    override fun showBankrupt(playerName: String) {
        val context = getApplication<Application>()
        gameMessage = context.getString(R.string.ShowBankruptMessage, playerName)
    }

    override fun askToBuyProperty(
        property: Property,
        player: Player,
        onDecision: (Boolean) -> Unit
    ) {
        buyProperty = onDecision
    }

    override fun askToSelectPropertyToBuild(
        properties: List<Property>,
        onSelected: (Property?) -> Unit
    ) {
        if (properties.isNotEmpty()) {
            // Build on first property, no select
            onSelected(properties.first())

        } else {
            // No properties to build in
            onSelected(null)
        }
    }

    override fun showGameOver(winner: Player) {
        val context = getApplication<Application>()
        this.winner = winner
        gameMessage = context.getString(R.string.WinnerShowMessage, winner.name)
        addLog("${context.getString(R.string.LogWinner)} ${winner.name}")
    }

    override fun updatePropertyOwner(playerId: Int, position: Int) {
        // No need
    }

    override fun showEndTurnButton(onEndTurn: () -> Unit) {
        endTurnAction = onEndTurn
    }


    /**
     * This functions does nothing, just calls compose to recompose the screen (for updating money and position)
     */
    private fun triggerPlayerRecomposition(playerId: Int) {
        val index = playersState.indexOfFirst { it.id == playerId }
        if (index != -1) {
            playersState[index] = playersState[index]
        }
    }

    /**
     * Builds the log
     */
    private fun addLog(message: String) {
        logBuilder += "$message\n"
    }
}

// Create the factory as the view has dependency
class GameViewModelFactory(
    private val application: Application,
    private val numPlayers: Int,
    private val playerNames: List<String>,
    private val initialMinutes: Int,
    private val startMoney: Int,
    private val passGoMoney: Int,
    private val jailTurns: Int,
    private val taxPrice: Int
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GameViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return GameViewModel(
                application,
                numPlayers,
                playerNames,
                initialMinutes,
                startMoney,
                passGoMoney,
                jailTurns,
                taxPrice
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}