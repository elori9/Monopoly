package com.example.monopoly.ui.viewmodel

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.monopoly.data.DataStoreManager
import kotlinx.coroutines.launch

class ConfigActivityViewModel(application: Application) : AndroidViewModel(application) {
    private val dataStoreManager = DataStoreManager(application)

    init {
        viewModelScope.launch {
            dataStoreManager.userPreferencesFlow.collect { preferences ->
                // Only update if it's the first load or if you want it to always reflect datastore
                // Flow emits the current state immediately upon subscription
                numPlayers = if (preferences.numPlayers == 0) 4 else preferences.numPlayers

                playersNamesList.clear()
                playersNamesList.addAll(preferences.playerNames)
                while (playersNamesList.size < 4) {
                    playersNamesList.add("P${playersNamesList.size + 1}")
                }

                isTimerEnabled = preferences.isTimerEnabled
                timeLimitText = preferences.timeLimitText
                advancedConfigEnabled = preferences.advancedConfigEnabled
                startMoney = preferences.startMoney.ifEmpty { "2000" }
                passGoMoney = preferences.passGoMoney.ifEmpty { "200" }
                jailTurns = preferences.jailTurns.ifEmpty { "3" }
                taxesPrice = preferences.taxesPrice.ifEmpty { "200" }
            }
        }
    }

    // Variables
    var numPlayers by mutableIntStateOf(4) // default to 4 so it's not 0 before load
        private set

    // Custom saver for the SnapshotStateList of player names
    val playersNamesList = mutableStateListOf("P1", "P2", "P3", "P4")
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

    fun saveSettings() {
        viewModelScope.launch {
            dataStoreManager.savePreferences(
                numPlayers = numPlayers,
                playerNames = playersNamesList,
                isTimerEnabled = isTimerEnabled,
                timeLimitText = timeLimitText,
                advancedConfigEnabled = advancedConfigEnabled,
                startMoney = startMoney,
                passGoMoney = passGoMoney,
                jailTurns = jailTurns,
                taxesPrice = taxesPrice
            )
        }
    }
}
