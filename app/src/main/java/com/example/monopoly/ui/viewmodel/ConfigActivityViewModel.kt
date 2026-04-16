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
}
