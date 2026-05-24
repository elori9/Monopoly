package com.example.monopoly.ui.broadcastreceivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.example.monopoly.R
import com.example.monopoly.ui.services.MusicService

class MusicBroadcast : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val i = Intent(context, MusicService::class.java)
        when (intent?.extras?.getString("ACTION")) {
            "PLAY" -> {
                i.putExtra("ACTION", "PLAY")
                Toast.makeText(
                    context,
                    context?.getString(R.string.StartingMusic),
                    Toast.LENGTH_LONG
                ).show()
            }

            "STOP" -> {
                i.putExtra("ACTION", "STOP")
                Toast.makeText(
                    context,
                    context?.getString(R.string.StoppingMusic),
                    Toast.LENGTH_LONG
                ).show()
            }
            // Shouldn't happen
            else -> ""
        }
        context?.startService(i)
    }
}
