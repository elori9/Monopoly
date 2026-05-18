package com.example.monopoly.ui.screens

import android.content.res.Configuration
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.monopoly.R
import com.example.monopoly.ui.components.ScreensHeaderArea
import com.example.monopoly.ui.theme.MonopolyTheme
import com.example.monopoly.ui.viewmodel.ConfigActivityViewModel

/**
 * Stateful Composable that manages the state for the configuration screen.
 */
@Composable
fun ConfigScreen(
    modifier: Modifier = Modifier,
    viewModel: ConfigActivityViewModel,
    onNavigateBack: () -> Unit,
) {
    val context = LocalContext.current

    // Detect orientation
    val isPortrait = LocalConfiguration.current.orientation == Configuration.ORIENTATION_PORTRAIT

    ConfigContent(
        isPortrait = isPortrait,
        numPlayers = viewModel.numPlayers,
        playerNames = viewModel.playerNames,
        isTimerEnabled = viewModel.isTimerEnabled,
        timeLimitText = viewModel.timeLimitText,
        onNumPlayersChange = { viewModel.updateNumPlayers(it) },
        onPlayerNameChange = { index, name -> viewModel.updatePlayerName(index, name) },
        onTimerToggle = { viewModel.toggleTimer(it) },
        onTimeLimitChange = { viewModel.updateTimeLimit(it) },
        onExit = { onNavigateBack() },
        isAdvancedExpanded = viewModel.advancedConfigEnabled,
        startingMoney = viewModel.startMoney,
        passGoMoney = viewModel.passGoMoney,
        jailTurns = viewModel.jailTurns,
        taxesPrice = viewModel.taxesPrice,
        onAdvancedToggle = { viewModel.toggleAdvancedConfig(it) },
        onStartingMoneyChange = { viewModel.updateStartingMoney(it) },
        onPassGoMoneyChange = { viewModel.updatePassGoMoney(it) },
        onJailTurnsChange = { viewModel.updateJailTurns(it) },
        onTaxesPriceChange = { viewModel.updateTaxesPrice(it) },
        onSave = {
            viewModel.saveSettings()
            Toast.makeText(context, context.getString(R.string.ConfSaved), Toast.LENGTH_SHORT)
                .show()
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
    isAdvancedExpanded: Boolean,
    startingMoney: String,
    passGoMoney: String,
    jailTurns: String,
    taxesPrice: String,
    onAdvancedToggle: (Boolean) -> Unit,
    onStartingMoneyChange: (String) -> Unit,
    onPassGoMoneyChange: (String) -> Unit,
    onJailTurnsChange: (String) -> Unit,
    onTaxesPriceChange: (String) -> Unit,
    onSave: () -> Unit,
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
            isAdvancedExpanded = isAdvancedExpanded,
            startingMoney = startingMoney,
            passGoMoney = passGoMoney,
            jailTurns = jailTurns,
            taxesPrice = taxesPrice,
            onAdvancedToggle = onAdvancedToggle,
            onStartingMoneyChange = onStartingMoneyChange,
            onPassGoMoneyChange = onPassGoMoneyChange,
            onJailTurnsChange = onJailTurnsChange,
            onTaxesPriceChange = onTaxesPriceChange,
            onSave = onSave,
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
            isAdvancedExpanded = isAdvancedExpanded,
            startingMoney = startingMoney,
            passGoMoney = passGoMoney,
            jailTurns = jailTurns,
            taxesPrice = taxesPrice,
            onAdvancedToggle = onAdvancedToggle,
            onStartingMoneyChange = onStartingMoneyChange,
            onPassGoMoneyChange = onPassGoMoneyChange,
            onJailTurnsChange = onJailTurnsChange,
            onTaxesPriceChange = onTaxesPriceChange,
            onSave = onSave,
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
    isAdvancedExpanded: Boolean,
    startingMoney: String,
    passGoMoney: String,
    jailTurns: String,
    taxesPrice: String,
    onAdvancedToggle: (Boolean) -> Unit,
    onStartingMoneyChange: (String) -> Unit,
    onPassGoMoneyChange: (String) -> Unit,
    onJailTurnsChange: (String) -> Unit,
    onTaxesPriceChange: (String) -> Unit,
    onSave: () -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Header
        item {
            ScreensHeaderArea(
                onExit = onExit,
                modifier = Modifier,
                title = stringResource(id = R.string.Configuration)
            )
            Spacer(modifier = Modifier.height(24.dp))
        }

        item {
            // Select players number
            PlayerCountSelector(
                currentCount = numPlayers,
                onCountSelected = onNumPlayersChange
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        items(numPlayers) { index ->
            PlayerNameInputs(
                index = index,
                name = playerNames[index],
                onNameChange = { newName -> onPlayerNameChange(index, newName) }
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        item {
            // Timer
            TimerConfiguration(
                isEnabled = isTimerEnabled,
                timeLimitText = timeLimitText,
                onToggle = onTimerToggle,
                onTimeChange = onTimeLimitChange
            )
            Spacer(modifier = Modifier.height(24.dp))
        }

        item {
            // Advanced options
            AdvancedConfiguration(
                isEnabled = isAdvancedExpanded,
                startingMoney = startingMoney,
                passGoMoney = passGoMoney,
                jailTurns = jailTurns,
                taxesPrice = taxesPrice,
                onAdvancedToggle = onAdvancedToggle,
                onStartingMoneyChange = onStartingMoneyChange,
                onPassGoMoneyChange = onPassGoMoneyChange,
                onJailTurnsChange = onJailTurnsChange,
                onTaxesPriceChange = onTaxesPriceChange,
            )
            Spacer(modifier = Modifier.height(24.dp))
        }

        item {
            SaveSettingsButton(onClick = onSave)
            Spacer(modifier = Modifier.height(24.dp))
        }
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
    isAdvancedExpanded: Boolean,
    startingMoney: String,
    passGoMoney: String,
    jailTurns: String,
    taxesPrice: String,
    onAdvancedToggle: (Boolean) -> Unit,
    onStartingMoneyChange: (String) -> Unit,
    onPassGoMoneyChange: (String) -> Unit,
    onJailTurnsChange: (String) -> Unit,
    onTaxesPriceChange: (String) -> Unit,
    onSave: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        ScreensHeaderArea(
            onExit = onExit,
            modifier = Modifier,
            title = stringResource(id = R.string.Configuration)
        )
        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // Left side: Players info + timer
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
            ) {
                // Players count
                item {
                    PlayerCountSelector(
                        currentCount = numPlayers,
                        onCountSelected = onNumPlayersChange
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }

                // Players names
                items(numPlayers) { index ->
                    PlayerNameInputs(
                        index = index,
                        name = playerNames[index],
                        onNameChange = { newName -> onPlayerNameChange(index, newName) }
                    )
                }

                // Timer
                item {
                    TimerConfiguration(
                        isEnabled = isTimerEnabled,
                        timeLimitText = timeLimitText,
                        onToggle = onTimerToggle,
                        onTimeChange = onTimeLimitChange
                    )
                }
            }

            // Right side: advanced options + start button
            LazyColumn(
                modifier = Modifier
                    .weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // Advanced options
                item {
                    AdvancedConfiguration(
                        isEnabled = isAdvancedExpanded,
                        startingMoney = startingMoney,
                        passGoMoney = passGoMoney,
                        jailTurns = jailTurns,
                        taxesPrice = taxesPrice,
                        onAdvancedToggle = onAdvancedToggle,
                        onStartingMoneyChange = onStartingMoneyChange,
                        onPassGoMoneyChange = onPassGoMoneyChange,
                        onJailTurnsChange = onJailTurnsChange,
                        onTaxesPriceChange = onTaxesPriceChange,
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(24.dp))
                    SaveSettingsButton(onClick = onSave)
                }
            }
        }
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
    index: Int,
    name: String,
    onNameChange: (String) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "${stringResource(R.string.Player)} ${index + 1}: ",
            fontSize = 20.sp,
            modifier = Modifier.width(100.dp)
        )
        OutlinedTextField(
            value = name,
            onValueChange = onNameChange,
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
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
                label = { Text(stringResource(R.string.TimerTimeLimit)) },
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

@Composable
fun SaveSettingsButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .height(56.dp)
            .width(200.dp),
        shape = RoundedCornerShape(8.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF4CAF50),
            contentColor = Color.White
        )
    ) {
        Text(text = stringResource(R.string.Save), fontSize = 20.sp, color = Color.White)
    }
}

@Composable
fun AdvancedConfiguration(
    isEnabled: Boolean,
    startingMoney: String,
    passGoMoney: String,
    jailTurns: String,
    taxesPrice: String,
    onAdvancedToggle: (Boolean) -> Unit,
    onStartingMoneyChange: (String) -> Unit,
    onPassGoMoneyChange: (String) -> Unit,
    onJailTurnsChange: (String) -> Unit,
    onTaxesPriceChange: (String) -> Unit,
) {
    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            Text(
                text = stringResource(R.string.ActivateAdvancedConfiguration),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f)
            )
            Checkbox(
                checked = isEnabled,
                onCheckedChange = onAdvancedToggle,
                colors = CheckboxDefaults.colors(checkedColor = Color(0xFF03A9F4))
            )
        }

        if (isEnabled) {
            // Starting money
            OutlinedTextField(
                value = startingMoney,
                onValueChange = { input ->
                    if (input.all { it.isDigit() }) onStartingMoneyChange(input)
                },
                label = { Text(stringResource(id = R.string.StartMoney)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            )

            // Pass go money
            OutlinedTextField(
                value = passGoMoney,
                onValueChange = { input ->
                    if (input.all { it.isDigit() }) onPassGoMoneyChange(input)
                },
                label = { Text(stringResource(id = R.string.PassGoMoney)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            )

            // Jail turns
            OutlinedTextField(
                value = jailTurns,
                onValueChange = { input ->
                    if (input.all { it.isDigit() }) onJailTurnsChange(input)
                },
                label = { Text(stringResource(id = R.string.JailTurns)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            )

            // Taxes prices
            OutlinedTextField(
                value = taxesPrice,
                onValueChange = { input ->
                    if (input.all { it.isDigit() }) onTaxesPriceChange(input)
                },
                label = { Text(stringResource(id = R.string.TaxesPrices)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            )
        }
    }
}

@Preview(showBackground = true, heightDp = 550, widthDp = 950)
@Composable
fun AdvancedConfigPreview() {
    var numPlayers by remember { mutableIntStateOf(4) }
    val playerNames = remember { mutableStateListOf("P1", "P2", "P3", "P4") }
    var timerEnabled by remember { mutableStateOf(true) }
    var timeText by remember { mutableStateOf("1") }

    var advancedExpanded by remember { mutableStateOf(true) }
    var startingMoney by remember { mutableStateOf("2000") }
    var passGoMoney by remember { mutableStateOf("200") }
    var jailTurns by remember { mutableStateOf("3") }
    var taxesPrice by remember { mutableStateOf("200") }

    MonopolyTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            ConfigContent(
                isPortrait = false,
                numPlayers = numPlayers,
                playerNames = playerNames,
                isTimerEnabled = timerEnabled,
                timeLimitText = timeText,
                onNumPlayersChange = { numPlayers = it },
                onPlayerNameChange = { index, name -> playerNames[index] = name },
                onTimerToggle = { timerEnabled = it },
                onTimeLimitChange = { timeText = it },
                onExit = { },
                isAdvancedExpanded = advancedExpanded,
                startingMoney = startingMoney,
                passGoMoney = passGoMoney,
                jailTurns = jailTurns,
                taxesPrice = taxesPrice,
                onAdvancedToggle = { advancedExpanded = it },
                onStartingMoneyChange = { startingMoney = it },
                onPassGoMoneyChange = { passGoMoney = it },
                onJailTurnsChange = { jailTurns = it },
                onTaxesPriceChange = { taxesPrice = it },
                onSave = { }
            )
        }
    }
}
