package game.controller

import game.interfaces.GameView
import game.model.Board
import game.model.Dice
import game.model.MessageType
import game.model.Player
import game.model.TurnAction
import game.model.box.*

class GameController(
    private var view: GameView,
    private val board: Board,
    private val players: List<Player>,
    private val timeLimit: Int = 0,
    private val dice: Dice

) {
    var turn: Int = 0
        private set
    var onGame: Boolean = false
    private var startTime: Long = 0L
    var winner: Player? = null
        private set


    /**
     * Starts a new game with the params
     */
    fun startGame() {
        onGame = true
        startTime = System.currentTimeMillis()
        view.showMessage(MessageType.START_GAME)
        nextTurn()
    }

    /**
     * Execute a turn
     */
    fun nextTurn() {
        // --- INITIAL VERIFICATIONS ---

        // Check time (DSS says end of the play turn, but at the start of there is better)
        if (checkTime()) {
            endGame()
            return
        }

        // End game for just one player
        val activePlayers = players.filter { !it.broke }
        if (activePlayers.size == 1) {
            winner = activePlayers.first()
            view.showGameOver(winner!!)
            return
        }

        // Get the player
        val currentPlayer: Player = players[turn]

        // Skip turn for broke players automatically
        if (currentPlayer.broke) {
            turn = (turn + 1) % players.size
            nextTurn()
            return
        }

        // Check jail
        if (currentPlayer.jail) {
            if (currentPlayer.jailTurns == 0) {
                // Get out of the jail
                currentPlayer.jail = false
            } else {
                // Count jail turn
                currentPlayer.jailTurns -= 1
                // End turn automatically without showing buttons
                endTurnAndPass()
                return
            }
        }

        // --- SHOW THE OPTIONS ---
        view.showTurnOptions(currentPlayer) { action ->
            when (action) {
                TurnAction.ROLL_DICE -> {
                    playTurn(currentPlayer)
                }

                TurnAction.BUILD_HOUSE -> {
                    buildHouse(currentPlayer)
                }
            }
        }
    }

    private fun playTurn(currentPlayer: Player) {
        // Roll the dice and show it
        val roll = dice.roll()
        view.showDiceRoll(currentPlayer.name, roll)

        // Save positions
        val oldPosition = currentPlayer.position
        val newPosition = (oldPosition + roll) % board.size

        // Move the player
        currentPlayer.move(newPosition)

        // Detect the start
        if (newPosition < oldPosition) {
            val startBox = board.getBox(0)
            if (startBox is Start) {
                startBox.action(currentPlayer)
                view.updatePlayerMoney(currentPlayer.id, currentPlayer.money)
                view.showMessage(MessageType.CROSS_START, currentPlayer.name)
            }
        }

        // Evaluate position
        val box = board.getBox(newPosition)

        // Show the player on the board
        view.updatePlayerPosition(currentPlayer.id, newPosition)

        // Show the options depending on the box
        showOptionsPosition(currentPlayer, box)
    }

    private fun endTurnAndPass() {
        // Wait for user to end turn
        view.showEndTurnButton(
            onEndTurn = {
                // Next turn
                turn = (turn + 1) % players.size
                nextTurn()
            }
        )
    }

    private fun buildHouse(currentPlayer: Player) {
        // Select the house
        val canBuild = currentPlayer.properties.filter {
            // Just 5 houses max and need to have the money
            it.numHouses < 5 && currentPlayer.money >= it.housePrice
        }
        if (canBuild.isEmpty()) {
            view.showMessage(MessageType.BUILD_CANCELED_NO_MONEY, currentPlayer.name)
            // Ask for his turn, as he didn't roll the dices
            nextTurn()
            return
        }
        view.askToSelectPropertyToBuild(canBuild) { selectedProperty ->
            if (selectedProperty != null) {
                // Validate the buy and update the money
                currentPlayer.buyHouse(selectedProperty)

                // Put the house on the board and update the money on the board
                view.updatePlayerMoney(currentPlayer.id, currentPlayer.money)
                view.showMessage(MessageType.HOUSE_BUILT, currentPlayer.name, selectedProperty.name)
                // Ask for his turn, as he didn't roll the dices
                nextTurn()
            } else {
                view.showMessage(MessageType.BUILD_CANCELED, currentPlayer.name)
                // Ask for his turn, as he didn't roll the dices
                nextTurn()
            }
        }
    }

    private fun showOptionsPosition(player: Player, gameBox: GameBox) {
        when (gameBox) {
            // Switch on different options
            is Property -> {
                if (gameBox.owner == null) {
                    // Ask to buy on
                    view.askToBuyProperty(gameBox, player) { buy ->
                        if (buy) {
                            if (player.money >= gameBox.price) {
                                // Buy it
                                player.buyProperty(gameBox)

                                // Update on gui
                                view.updatePlayerMoney(player.id, player.money)
                                view.updatePropertyOwner(player.id, player.position)
                                view.showMessage(MessageType.PROPERTY_BOUGHT, player.name, gameBox.name)
                            } else {
                                view.showMessage(MessageType.CANT_BUY_PROP, player.name)
                            }
                        }
                        // End the turn
                        endTurnAndPass()
                    }
                } else {
                    // Someone is the owner
                    gameBox.action(player)

                    // Player can't pay
                    if (player.broke) {
                        view.showBankrupt(player.name)
                    }

                    // Update the gui
                    view.updatePlayerMoney(player.id, player.money)

                    // If I got deposit show it too
                    if (gameBox.owner != player) {
                        // Use !! bc owner can't be null on this point
                        view.updatePlayerMoney(gameBox.owner!!.id, gameBox.owner!!.money)
                        view.showMessage(MessageType.RENT_PAID, player.name, gameBox.name)
                    }

                    // End the turn
                    endTurnAndPass()
                }
            }

            is Fee -> {
                gameBox.action(player)
                view.updatePlayerMoney(player.id, player.money)
                view.showMessage(MessageType.FEE_PAID, player.name)

                // Check if player broke
                if (player.broke) {
                    view.showBankrupt(player.name)
                }

                // End the turn
                endTurnAndPass()
            }

            is Card -> {
                val result = gameBox.action(player)
                view.showMessage(MessageType.CARD, player.name, result)

                if (result.contains("€")) {
                    // Show the money update
                    view.updatePlayerMoney(player.id, player.money)

                    // Check player broke
                    if (player.broke) {
                        view.showBankrupt(player.name)
                    }

                    // End the turn
                    endTurnAndPass()
                } else {
                    // Show the position update
                    view.updatePlayerPosition(player.id, player.position)

                    // Ask for another action on the new box
                    val newBox = board.getBox(player.position)
                    showOptionsPosition(player, newBox)
                }
            }

            is Jail -> {
                gameBox.action(player)
                view.showMessage(MessageType.GO_TO_JAIL, player.name)

                // End the turn
                endTurnAndPass()
            }

            is Start -> {
                view.showMessage(MessageType.CROSS_START, player.name)

                // End the turn
                endTurnAndPass()
            }
        }
    }

    /**
     * Check the time of the game
     */
    fun checkTime(): Boolean {
        if (timeLimit == 0) return false

        val currentTime = System.currentTimeMillis()
        val timePassedMillis = currentTime - startTime

        // convert the minutes to ms
        val limitMillis = timeLimit * 60 * 1000

        return timePassedMillis >= limitMillis
    }

    /**
     * Select a winner
     */
    fun endGame() {
        // Search for the max Networh player
        winner = players.maxByOrNull { it.calculateNetworth() }

        // Show the winner
        winner?.let {
            view.showGameOver(it)
        }
    }

    // --- Setters and getters ---
    /**
     * Inject the Mock
     */
    fun setGameView(gameView: GameView) {
        view = gameView
    }

    /**
     * Set start time for testing
     */
    fun setStartTime(time: Long) {
        startTime = time
    }
}