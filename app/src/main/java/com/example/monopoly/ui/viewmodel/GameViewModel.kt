package com.example.monopoly.ui.viewmodel


import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.monopoly.R
import game.interfaces.GameView
import game.model.Player
import game.model.TurnAction
import game.model.box.Property


class GameViewModel : GameView {
    // State vars
    val playersState = mutableStateListOf<Player>()
    var currentPlayerMoney by mutableIntStateOf(0)
    var gameMessage by mutableStateOf("")
    var dice by mutableStateOf<Int?>(null)
    var currentPlayer by mutableStateOf<Player?>(null)
    var winner by mutableStateOf<Player?>(null)
        private set

    // Those will be the callbacks
    var turnAction: ((TurnAction) -> Unit)? by mutableStateOf(null)
    var buyProperty: ((Boolean) -> Unit)? by mutableStateOf(null)
    var endTurnAction: (() -> Unit)? by mutableStateOf(null)


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
        gameMessage = "$playerName roll $roll"
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
        gameMessage = "$playerName bankrupt"
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

    override fun showGameOver(winner: Player){
        this.winner = winner
        gameMessage = "${winner.name} winn"
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
}