package com.example.monopoly.data

import android.app.Application

class LogApplication : Application() {
    val database by lazy { LogDatabase.getDatabase(this) }
    val repository by lazy { LogRepository(database.logDao()) }
}
