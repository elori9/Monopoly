package com.example.monopoly.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class DataStoreManager(val context: Context) {

    companion object {
        val NUM_PLAYERS = intPreferencesKey("num_players")
        val PLAYER_NAMES = stringPreferencesKey("player_names")
        val TIMER_ENABLED = booleanPreferencesKey("timer_enabled")
        val TIME_LIMIT = stringPreferencesKey("time_limit")
        val ADVANCED_CONFIG = booleanPreferencesKey("advanced_config")
        val START_MONEY = stringPreferencesKey("start_money")
        val PASS_GO_MONEY = stringPreferencesKey("pass_go_money")
        val JAIL_TURNS = stringPreferencesKey("jail_turns")
        val TAXES_PRICE = stringPreferencesKey("taxes_price")
    }

    suspend fun savePreferences(
        numPlayers: Int,
        playerNames: List<String>,
        isTimerEnabled: Boolean,
        timeLimitText: String,
        advancedConfigEnabled: Boolean,
        startMoney: String,
        passGoMoney: String,
        jailTurns: String,
        taxesPrice: String
    ) {
        context.dataStore.edit { preferences ->
            preferences[NUM_PLAYERS] = numPlayers
            preferences[PLAYER_NAMES] = playerNames.joinToString(",")
            preferences[TIMER_ENABLED] = isTimerEnabled
            preferences[TIME_LIMIT] = timeLimitText
            preferences[ADVANCED_CONFIG] = advancedConfigEnabled
            preferences[START_MONEY] = startMoney
            preferences[PASS_GO_MONEY] = passGoMoney
            preferences[JAIL_TURNS] = jailTurns
            preferences[TAXES_PRICE] = taxesPrice
        }
    }

    val userPreferencesFlow: Flow<UserPreferences> = context.dataStore.data.map { preferences ->
        UserPreferences(
            numPlayers = preferences[NUM_PLAYERS] ?: 0,
            playerNames = preferences[PLAYER_NAMES]?.split(",")?.let { names ->
                names.mapIndexed { index, name -> name.ifEmpty { "P${index + 1}" } }
            } ?: listOf("P1", "P2", "P3", "P4"),
            isTimerEnabled = preferences[TIMER_ENABLED] ?: false,
            timeLimitText = preferences[TIME_LIMIT] ?: "",
            advancedConfigEnabled = preferences[ADVANCED_CONFIG] ?: false,
            startMoney = preferences[START_MONEY] ?: "2000",
            passGoMoney = preferences[PASS_GO_MONEY] ?: "200",
            jailTurns = preferences[JAIL_TURNS] ?: "3",
            taxesPrice = preferences[TAXES_PRICE] ?: "200"
        )
    }
}

data class UserPreferences(
    val numPlayers: Int,
    val playerNames: List<String>,
    val isTimerEnabled: Boolean,
    val timeLimitText: String,
    val advancedConfigEnabled: Boolean,
    val startMoney: String,
    val passGoMoney: String,
    val jailTurns: String,
    val taxesPrice: String
)
