package com.example.monopoly.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "game_history_table")
data class LogEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val date: String,
    val winnerName: String,
    val totalTurns: Int,
    val durationMinutes: Int,
    val logLine: String,
)
