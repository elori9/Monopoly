package com.example.monopoly.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class ResultsViewModel : ViewModel() {

    // Variables
    var email by mutableStateOf("")
        private set

    var logInfo by mutableStateOf("")
        private set


    // Functions
    fun updateEmail(newEmail: String) {
        email = newEmail
    }

}
