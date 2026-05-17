package com.example.monopoly.data

import androidx.annotation.WorkerThread
import kotlinx.coroutines.flow.Flow

class LogRepository(private val logDao: LogDao) {
    val allLogs: Flow<List<LogEntity>> = logDao.getAllLogs()

    @WorkerThread
    suspend fun insertLog(gameLogEntity: LogEntity) {
        logDao.insertLog(gameLogEntity)
    }

    @WorkerThread
    suspend fun deleteLog(gameLogEntity: LogEntity) {
        logDao.deleteLog(gameLogEntity)
    }

    @WorkerThread
    suspend fun deleteAllLogs() {
        logDao.deleteAllLogs()
    }
}
