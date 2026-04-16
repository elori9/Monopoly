package com.example.monopoly.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class ConfigActivityViewModel : ViewModel() {

    // Variables
    var numPlayers by mutableIntStateOf(0)
        private set

    // Custom saver for the SnapshotStateList of player names
    val playersNamesList = mutableStateListOf("", "", "", "")
    val playerNames: List<String> get() = playersNamesList

    var isTimerEnabled by mutableStateOf(false)
        private set
    var timeLimitText by mutableStateOf("")
        private set

    // Advanced configuration variables
    var advancedConfigEnabled by mutableStateOf(false)
        private set
    var startMoney by mutableStateOf("2000")
        private set
    var passGoMoney by mutableStateOf("200")
        private set
    var jailTurns by mutableStateOf("3")
        private set
    var taxesPrice by mutableStateOf("200")
        private set

    // Functions
    fun updateNumPlayers(count: Int) {
        numPlayers = count
    }

    fun updatePlayerName(index: Int, name: String) {
        playersNamesList[index] = name
    }

    fun toggleTimer(isEnabled: Boolean) {
        isTimerEnabled = isEnabled
    }

    fun updateTimeLimit(time: String) {
        timeLimitText = time
    }

    fun areAllNamesFilled(): Boolean {
        return getSelectedPlayerNames().none { it.isBlank() }
    }

    fun getSelectedPlayerNames(): List<String> {
        return playersNamesList.take(numPlayers)
    }

    fun getFinalTimeLimit(): Int {
        // If is enabled use the time, otherwise no time -> 0
        return if (isTimerEnabled) timeLimitText.toIntOrNull() ?: 0 else 0
    }

    // Advanced configuration functions

    fun toggleAdvancedConfig(isExpanded: Boolean) {
        advancedConfigEnabled = isExpanded
    }

    fun updateStartingMoney(amount: String) {
        startMoney = amount
    }

    fun updatePassGoMoney(amount: String) {
        passGoMoney = amount
    }

    fun updateJailTurns(turns: String) {
        jailTurns = turns
    }

    fun updateTaxesPrice(price: String) {
        taxesPrice = price
    }
}
