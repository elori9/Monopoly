package game.controller

import game.interfaces.GameView
import game.model.Player
import game.model.TurnAction
import game.model.box.Property

/**
 * Mock for testing GameController.
 */
class MockGameView : GameView {

    var lastMessage: String = ""
    var lastDiceRoll: Int = 0
    var isGameOver: Boolean = false
    var winnerName: String? = null
    var bankruptcies = mutableListOf<String>()
    var turnsToPlay = 2
    var turnsPlayed = 0

    // Responses configuration
    var defaultTurnAction: TurnAction = TurnAction.ROLL_DICE
    var buyProperty: Boolean = true
    var propertyToBuild: Property? = null


    override fun showMessage(message: String) {
        lastMessage = message
        println("GUI: $message")
    }

    override fun showDiceRoll(playerName: String, roll: Int) {
        lastDiceRoll = roll
        println("GUI: $playerName rolled a $roll")
    }

    override fun updatePlayerPosition(playerId: Int, newPosition: Int) {
        println("GUI: Player $playerId moved to box $newPosition")
    }

    override fun updatePlayerMoney(playerId: Int, money: Int) {
        // No need to test that as is only an animation
    }

    override fun showBankrupt(playerName: String) {
        bankruptcies.add(playerName)
        println("GUI: BANKRUPT for $playerName")
    }

    override fun askToBuyProperty(property: Property, player: Player, onDecision: (Boolean) -> Unit) {
        onDecision(buyProperty)
    }

    override fun askToSelectPropertyToBuild(properties: List<Property>, onSelected: (Property?) -> Unit) {
        onSelected(propertyToBuild)
    }

    override fun showGameOver(winner: Player) {
        isGameOver = true
        winnerName = winner.name
        println("GUI: End, winner: ${winner.name}")
    }

    override fun updatePropertyOwner(playerId: Int, position: Int) {
        println("GUI: New owner on $position for the player $playerId")
    }

    override fun showEndTurnButton(onEndTurn: () -> Unit) {
        onEndTurn()
    }


    override fun showTurnOptions(player: Player, onActionSelected: (TurnAction) -> Unit) {
        // Make n turns for testing
        if (turnsPlayed < turnsToPlay) {
            turnsPlayed++
            onActionSelected(defaultTurnAction)
        } else {
            println("GUI: TEST PAUSED")
        }
    }
}