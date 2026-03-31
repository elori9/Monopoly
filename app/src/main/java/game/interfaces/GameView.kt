package game.interfaces

import game.model.Player
import game.model.box.Property

/**
 * Interface for testing on Android GUI
 *
 */
interface GameView {

    /**
     * Show a message
     * @param message the message
     */
    fun showMessage(message: String)

    /**
     * Show dice roll
     * @param playerName the player
     * @param roll the dice rolled number
     */
    fun showDiceRoll(playerName: String, roll: Int)

    /**
     * Update the player position
     * @param playerId the playerID
     * @param newPosition the new position
     */
    fun updatePlayerPosition(playerId: Int, newPosition: Int)

    /**
     * Update the player money
     * @param playerId the playerID
     * @param money the money
     */
    fun updatePlayerMoney(playerId: Int, money: Int)

    /**
     * Show bankrupt
     * @param playerName the player
     */
    fun showBankrupt(playerName: String)

    /**
     * Ask the player to buy property
     * @param property the property
     * @param player the player
     * @param onDecision the callback
     */
    fun askToBuyProperty(property: Property, player: Player, onDecision: (Boolean) -> Unit)

    /**
     * Ask the player to build a house
     * @param property the property
     * @param onDecision the callback
     */
    fun askToBuildHouse(property: Property, onDecision: (Boolean) -> Unit)

    /**
     * Show game over
     * @param winner the winner
     */
    fun showGameOver(winner: Player)

    /**
     * Update the property owner
     * @param playerId the playerID
     * @param position the position
     */
    fun updatePropertyOwner(playerId: Int, position: Int)
}