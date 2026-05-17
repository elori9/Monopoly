package com.example.monopoly.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface LogDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertLog(gameLogEntity: LogEntity)

    @Query("SELECT * FROM game_history_table ORDER BY id DESC")
    fun getAllLogs(): Flow<List<LogEntity>>

    @Delete
    suspend fun deleteLog(gameLogEntity: LogEntity)

    @Query("DELETE FROM game_history_table")
    suspend fun deleteAllLogs()
}
