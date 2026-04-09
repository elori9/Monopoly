package com.example.monopoly.ui.screens

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.example.monopoly.ui.screens.ui.theme.MonopolyTheme
import kotlin.system.exitProcess

class Results : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val gameInfo = intent?.getStringExtra("INFO") ?: "No game log"

        setContent {
            MonopolyTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    ResultsScreen(
                        info = gameInfo,
                        onSendEmail = {
                            // Send the email
                            val intent = Intent(Intent.ACTION_SEND)
                        },
                        onNewGame = {
                            val intent = Intent(this, MainActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                            startActivity(intent)
                            finish()
                        },
                        onExit = {
                            exitProcess(0)
                        },
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun ResultsScreen(
    info: String,
    onSendEmail: () -> Unit,
    onNewGame: () -> Unit,
    onExit: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()

    ) {
        // Title
        Text(
            text = "END",
        )

        // Log info
        Text(
            text = info,
        )

        // Send email button
        Button(
            onClick = onSendEmail,
            modifier = Modifier
        ) {
            Text("TEXT", fontSize = 18.sp)
        }

        // New Game button
        Button(
            onClick = onNewGame,
            modifier = Modifier
        ) {
            Text("TEXT", fontSize = 18.sp)
        }

        // Exit button
        Button(
            onClick = onExit,
            modifier = Modifier
        ) {
            Text("TEXT", fontSize = 18.sp)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ResultsPreview() {
    MonopolyTheme {
        ResultsScreen(
            info = "info",
            onNewGame = {},
            onSendEmail = {},
            onExit = {},
            )
    }
}