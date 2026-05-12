package com.example.monopoly.ui.viewmodel

import android.app.Application
import android.content.Context
import android.content.Intent
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.monopoly.ui.broadcastreceivers.MusicBroadcast
import androidx.compose.runtime.getValue
import androidx.lifecycle.AndroidViewModel

class MenuActivityViewModel(application: Application) : AndroidViewModel(application) {
    var isMusicPlaying by mutableStateOf(false)
        private set

    fun toggleMusic() {
        val context: Context = getApplication()
        val action = if (isMusicPlaying) "STOP" else "PLAY"
        isMusicPlaying = !isMusicPlaying

        val intent = Intent(context, MusicBroadcast::class.java)
        intent.putExtra("ACTION", action)
        context.sendBroadcast(intent)
    }
}
