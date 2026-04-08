package com.example.monopoly.ui.screens

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.monopoly.R
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
    
    // Use rememberSaveable for basic types
    var numPlayers by rememberSaveable { mutableIntStateOf(0) }
    
    // Custom saver for the SnapshotStateList of player names
    val playerNames = rememberSaveable(saver = listSaver(
        save = { it.toList() },
        restore = { mutableStateListOf(*it.toTypedArray()) }
    )) { mutableStateListOf("", "", "", "") }
    
    var isTimerEnabled by rememberSaveable { mutableStateOf(false) }
    var timeLimitText by rememberSaveable { mutableStateOf("") }

    // Detect orientation
    val isPortrait = LocalConfiguration.current.orientation == Configuration.ORIENTATION_PORTRAIT

    ConfigContent(
        isPortrait = isPortrait,
        numPlayers = numPlayers,
        playerNames = playerNames,
        isTimerEnabled = isTimerEnabled,
        timeLimitText = timeLimitText,
        onNumPlayersChange = { numPlayers = it },
        onPlayerNameChange = { index, name -> playerNames[index] = name },
        onTimerToggle = { isTimerEnabled = it },
        onTimeLimitChange = { timeLimitText = it },
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
                // If is enabled use the time, otherwise no time -> 0
                val finalTimeLimit = if (isTimerEnabled) timeLimitText.toIntOrNull() ?: 0 else 0

                val intent = Intent(context, GameActivity::class.java).apply {
                    putExtra("NUM_PLAYERS", numPlayers)
                    putStringArrayListExtra("PLAYER_NAMES", ArrayList(selectedNames))
                    putExtra("TIME_LIMIT", finalTimeLimit)
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
    isPortrait: Boolean,
    numPlayers: Int,
    playerNames: List<String>,
    isTimerEnabled: Boolean,
    timeLimitText: String,
    onNumPlayersChange: (Int) -> Unit,
    onPlayerNameChange: (Int, String) -> Unit,
    onTimerToggle: (Boolean) -> Unit,
    onTimeLimitChange: (String) -> Unit,
    onExit: () -> Unit,
    onStartGame: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (isPortrait) {
        DrawConfigPortrait(
            numPlayers = numPlayers,
            playerNames = playerNames,
            isTimerEnabled = isTimerEnabled,
            timeLimitText = timeLimitText,
            onNumPlayersChange = onNumPlayersChange,
            onPlayerNameChange = onPlayerNameChange,
            onTimerToggle = onTimerToggle,
            onTimeLimitChange = onTimeLimitChange,
            onExit = onExit,
            onStartGame = onStartGame,
            modifier = modifier
        )
    } else {
        DrawConfigLandscape(
            numPlayers = numPlayers,
            playerNames = playerNames,
            isTimerEnabled = isTimerEnabled,
            timeLimitText = timeLimitText,
            onNumPlayersChange = onNumPlayersChange,
            onPlayerNameChange = onPlayerNameChange,
            onTimerToggle = onTimerToggle,
            onTimeLimitChange = onTimeLimitChange,
            onExit = onExit,
            onStartGame = onStartGame,
            modifier = modifier
        )
    }
}

@Composable
fun DrawConfigPortrait(
    numPlayers: Int,
    playerNames: List<String>,
    isTimerEnabled: Boolean,
    timeLimitText: String,
    onNumPlayersChange: (Int) -> Unit,
    onPlayerNameChange: (Int, String) -> Unit,
    onTimerToggle: (Boolean) -> Unit,
    onTimeLimitChange: (String) -> Unit,
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
        // Header
        ConfigHeader(onExit = onExit)

        Spacer(modifier = Modifier.height(24.dp))

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Select players number
            PlayerCountSelector(
                currentCount = numPlayers,
                onCountSelected = onNumPlayersChange
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Select players names
            PlayerNameInputs(
                numPlayers = numPlayers,
                playerNames = playerNames,
                onNameChange = onPlayerNameChange
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Timer
            TimerConfiguration(
                isEnabled = isTimerEnabled,
                timeLimitText = timeLimitText,
                onToggle = onTimerToggle,
                onTimeChange = onTimeLimitChange
            )
        }

        // Start button
        StartGameButton(
            isEnabled = numPlayers > 0,
            onClick = onStartGame
        )
    }
}

@Composable
fun DrawConfigLandscape(
    numPlayers: Int,
    playerNames: List<String>,
    isTimerEnabled: Boolean,
    timeLimitText: String,
    onNumPlayersChange: (Int) -> Unit,
    onPlayerNameChange: (Int, String) -> Unit,
    onTimerToggle: (Boolean) -> Unit,
    onTimeLimitChange: (String) -> Unit,
    onExit: () -> Unit,
    onStartGame: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        ConfigHeader(onExit = onExit)

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // Left side: Players info
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
            ) {
                PlayerCountSelector(
                    currentCount = numPlayers,
                    onCountSelected = onNumPlayersChange
                )
                Spacer(modifier = Modifier.height(16.dp))
                PlayerNameInputs(
                    numPlayers = numPlayers,
                    playerNames = playerNames,
                    onNameChange = onPlayerNameChange
                )
            }

            // Right side: Timer and Action
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                    TimerConfiguration(
                        isEnabled = isTimerEnabled,
                        timeLimitText = timeLimitText,
                        onToggle = onTimerToggle,
                        onTimeChange = onTimeLimitChange
                    )
                }

                StartGameButton(
                    isEnabled = numPlayers > 0,
                    onClick = onStartGame,
                    modifier = Modifier.padding(bottom = 0.dp) // No bottom padding needed in landscape
                )
            }
        }
    }
}

@Composable
fun ConfigHeader(onExit: () -> Unit) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = stringResource(id = R.string.Configuration),
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
    }
}

@Composable
fun PlayerCountSelector(
    currentCount: Int,
    onCountSelected: (Int) -> Unit
) {
    Text(
        text = stringResource(R.string.SelectPlayers),
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
                        selected = (currentCount == number),
                        onClick = { onCountSelected(number) },
                        role = Role.RadioButton
                    )
                    .padding(horizontal = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = (currentCount == number),
                    onClick = null,
                    colors = RadioButtonDefaults.colors(selectedColor = Color(0xFF4CAF50))
                )
                Text(
                    text = number.toString(),
                    fontSize = 24.sp,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
        }
    }
}

@Composable
fun PlayerNameInputs(
    numPlayers: Int,
    playerNames: List<String>,
    onNameChange: (Int, String) -> Unit
) {
    for (i in 0 until numPlayers) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.Player) + " ${i + 1}: ",
                fontSize = 24.sp,
                modifier = Modifier.width(120.dp)
            )
            OutlinedTextField(
                value = playerNames[i],
                onValueChange = { onNameChange(i, it) },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
fun TimerConfiguration(
    isEnabled: Boolean,
    timeLimitText: String,
    onToggle: (Boolean) -> Unit,
    onTimeChange: (String) -> Unit
) {
    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            Text(
                text = stringResource(R.string.AddTimer),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f)
            )
            Checkbox(
                checked = isEnabled,
                onCheckedChange = onToggle,
                colors = CheckboxDefaults.colors(checkedColor = Color(0xFF03A9F4))
            )
        }

        if (isEnabled) {
            OutlinedTextField(
                value = timeLimitText,
                onValueChange = { input ->
                    if (input.all { it.isDigit() }) onTimeChange(input)
                },
                label = { Text("Time Limit (minutes)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            )
        }
    }
}

@Composable
fun StartGameButton(
    isEnabled: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        enabled = isEnabled,
        modifier = modifier
            .padding(bottom = 32.dp)
            .height(56.dp)
            .width(200.dp),
        shape = RoundedCornerShape(8.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF03A9F4),
            disabledContainerColor = Color.LightGray
        )
    ) {
        Text(text = stringResource(R.string.StartGame), fontSize = 20.sp, color = Color.White)
    }
}
