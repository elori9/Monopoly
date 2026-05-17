package com.example.monopoly.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.monopoly.data.LogEntity
import com.example.monopoly.data.LogRepository
import kotlinx.coroutines.launch

class LogViewModel(private val repository: LogRepository) : ViewModel() {
    val allLogs = repository.allLogs

    /**
     * Launching a new coroutine to insert the data in a non-blocking way, suspend because of Nav patron
     */
    suspend fun insertLog(gameLogEntity: LogEntity) {
        repository.insertLog(gameLogEntity)
    }

    /**
     * Launching a new coroutine to delete the data in a non-blocking way, suspend because of Nav patron
     */
    suspend fun deleteLog(gameLogEntity: LogEntity) {
        repository.deleteLog(gameLogEntity)
    }

    /**
     * Launching a new coroutine to delete all the data in a non-blocking way, suspend because of Nav patron
     */
    suspend fun deleteAllLogs() {
        repository.deleteAllLogs()
    }
}

class LogViewModelFactory(private val repository: LogRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LogViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return LogViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
