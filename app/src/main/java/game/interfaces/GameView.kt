package game.interfaces

import game.model.MessageType
import game.model.Player
import game.model.TurnAction
import game.model.box.Property

/**
 * Interface for testing on Android GUI
 *
 */
interface GameView {

    /**
     * Show a message (non win, no dice roll and no bankrupt)
     * @param MessageType the type of message
     * @param extraInfo extra info
     */
    fun showMessage(type: MessageType, extraInfo: String? = null)

    /**
     * Show options on the start
     * @param player the player
     * @param onActionSelected the callback with the action
     */
    fun showTurnOptions(player: Player, onActionSelected: (TurnAction) -> Unit)

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
     * Ask the player to select a property to build a house
     * @param properties list of properties where the player can build
     * @param onSelected callback with the selected property
     */
    fun askToSelectPropertyToBuild(properties: List<Property>, onSelected: (Property?) -> Unit)

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

    /**
     * Will activate the end turn button
     */
    fun showEndTurnButton(onEndTurn: () -> Unit)
}