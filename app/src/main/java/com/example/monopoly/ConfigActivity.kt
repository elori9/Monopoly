package com.example.monopoly

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.monopoly.ui.theme.MonopolyTheme

class ConfigActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MonopolyTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    ConfigScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

/**
 * Stateful Composable that manages the state for the configuration screen.
 */
@Composable
fun ConfigScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    var numPlayers by remember { mutableIntStateOf(0) }
    val playerNames = remember { mutableStateListOf("", "", "", "") }

    ConfigContent(
        numPlayers = numPlayers,
        playerNames = playerNames,
        onNumPlayersChange = { numPlayers = it },
        onPlayerNameChange = { index, name -> playerNames[index] = name },
        onExit = {
            val intent = Intent(context, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            context.startActivity(intent)
        },
        onStartGame = {
            val selectedNames = playerNames.take(numPlayers)
            if (selectedNames.any { it.isBlank() }) {
                Toast.makeText(context, "Please enter all player names", Toast.LENGTH_SHORT).show()
            } else {
                val intent = Intent(context, GameActivity::class.java).apply {
                    putExtra("NUM_PLAYERS", numPlayers)
                    putStringArrayListExtra("PLAYER_NAMES", ArrayList(selectedNames))
                }
                context.startActivity(intent)
            }
        },
        modifier = modifier
    )
}

/**
 * Stateless Composable that only renders the UI based on the provided state and callbacks.
 */
@Composable
fun ConfigContent(
    numPlayers: Int,
    playerNames: List<String>,
    onNumPlayersChange: (Int) -> Unit,
    onPlayerNameChange: (Int, String) -> Unit,
    onExit: () -> Unit,
    onStartGame: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Configuration",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold
            )
            IconButton(onClick = onExit) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Exit",
                    tint = Color.Red,
                    modifier = Modifier.size(36.dp)
                )
            }
        }
        
        HorizontalDivider(thickness = 2.dp, color = Color.Black)

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Num. Players",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.fillMaxWidth()
        )

        Row(
            Modifier
                .selectableGroup()
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            listOf(2, 3, 4).forEach { number ->
                Row(
                    Modifier
                        .selectable(
                            selected = (numPlayers == number),
                            onClick = { onNumPlayersChange(number) },
                            role = Role.RadioButton
                        )
                        .padding(horizontal = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = (numPlayers == number),
                        onClick = null,
                        colors = RadioButtonDefaults.colors(
                            selectedColor = Color(0xFF4CAF50)
                        )
                    )
                    Text(
                        text = number.toString(),
                        fontSize = 24.sp,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        for (i in 0 until 4) {
            val isEnabled = i < numPlayers
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Player ${i + 1}: ",
                    fontSize = 24.sp,
                    color = if (isEnabled) Color.Unspecified else Color.LightGray,
                    modifier = Modifier.width(120.dp)
                )
                OutlinedTextField(
                    value = playerNames[i],
                    onValueChange = { onPlayerNameChange(i, it) },
                    enabled = isEnabled,
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        disabledBorderColor = Color.LightGray,
                        disabledTextColor = Color.LightGray
                    )
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = onStartGame,
            enabled = numPlayers > 0,
            modifier = Modifier
                .padding(bottom = 32.dp)
                .height(56.dp)
                .width(200.dp),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF03A9F4),
                disabledContainerColor = Color.LightGray
            )
        ) {
            Text(text = "Start Game", fontSize = 20.sp, color = Color.White)
        }
    }
}
