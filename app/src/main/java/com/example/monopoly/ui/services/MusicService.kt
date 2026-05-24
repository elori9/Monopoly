package com.example.monopoly.ui.services

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.Binder
import android.os.IBinder
import com.example.monopoly.R


class MusicService : Service() {

    private val localBinder = LocalBinder()

    private var mediaPlayer: MediaPlayer? = null


    override fun onCreate() {
        super.onCreate()
    }


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        when (intent?.getStringExtra("ACTION")) {
            "PLAY" -> {
                startMusic(R.raw.music)
            }
            "STOP" -> {
                stopMusic()
            }
        }
        return START_NOT_STICKY
    }

    private fun startMusic(song: Int) {
        // Stop the previous
        stopMusic()

        // Start the music
        mediaPlayer = MediaPlayer.create(this, song)
        mediaPlayer?.isLooping = true
        mediaPlayer?.start()
    }

    private fun stopMusic() {
        if (mediaPlayer != null) {
            mediaPlayer!!.stop()
            mediaPlayer!!.release()
            mediaPlayer = null
        }
    }

    override fun onBind(intent: Intent): IBinder {
        //super.onBind(intent)
        return localBinder
    }

    override fun onRebind(intent: Intent) {
        super.onRebind(intent)
    }

    override fun onUnbind(intent: Intent): Boolean {
        return true
    }


    override fun onDestroy() {
        stopMusic()
        super.onDestroy()
    }


    /**
     * Class used for the client Binder.  Since this service runs in the same process as its
     * clients, we don't need to deal with IPC.
     */
    inner class LocalBinder : Binder() {
        internal val service: MusicService get() = this@MusicService
    }


}