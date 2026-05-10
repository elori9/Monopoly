package com.example.monopoly.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.monopoly.R
import com.example.monopoly.ui.theme.MonopolyTheme
import java.util.Locale


/**
 * Stateless function: Purely visual representation of the header.
 */
@Composable
fun GameHeaderAreaPortrait(
    secondsRemaining: Long,
    isTimerEnabled: Boolean,
    currentPlayerName: String,
    currentPlayerId: Int,
    onExitGame: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // Timer on the left
        Box(modifier = Modifier.width(100.dp)) {
            GameTimer(
                secondsRemaining = secondsRemaining,
                isTimerEnabled = isTimerEnabled
            )
        }

        // Turn info in the middle
        TurnInfo(
            playerName = currentPlayerName,
            playerIconRes = getPlayerTokenById(currentPlayerId),
            modifier = Modifier.weight(1f)
        )

        // Buttons on the right
        Box(modifier = Modifier.width(100.dp), contentAlignment = Alignment.CenterEnd) {
            SmartHeaderButtons(
                onExitGame = onExitGame
            )
        }
    }
}

/**
 * Stateless function: draws the turn info with player icon and name
 */
@Composable
fun TurnInfo(
    playerName: String,
    playerIconRes: Int,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        Text(
            text = stringResource(id = R.string.CurrentTurn),
            fontSize = 12.sp,
            color = Color.Gray,
            fontWeight = FontWeight.Medium
        )
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(id = playerIconRes),
                contentDescription = null,
                modifier = Modifier.size(35.dp).padding(end = 8.dp)
            )
            Text(
                text = playerName,
                fontSize = 18.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color.Black
            )
        }
    }
}

/**
 * Helper function to map player ID to its corresponding icon resource.
 */
fun getPlayerTokenById(playerId: Int): Int {
    return when (playerId) {
        0 -> R.drawable.icon1
        1 -> R.drawable.icon2
        2 -> R.drawable.icon3
        3 -> R.drawable.icon4
        else -> R.drawable.icon1
    }
}

/**
 * Stateless function: draws the timer
 */
@Composable
fun GameTimer(
    secondsRemaining: Long,
    isTimerEnabled: Boolean,
    modifier: Modifier = Modifier
) {
    if (isTimerEnabled) {
        Text(
            text = stringResource(id = R.string.TimeLabel, formatTime(secondsRemaining)),
            fontSize = 16.sp,
            fontWeight = FontWeight.ExtraBold,
            color = Color.Black,
            modifier = modifier
        )
    } else {
        Spacer(modifier = modifier)
    }
}

/**
 * Stateless Icons: exit button, reutilizable for put more easily
 */
@Composable
fun HeaderButtons(
    showExitDialog: Boolean,
    onMenuClick: () -> Unit,
    onDismissExitDialog: () -> Unit,
    onConfirmExitGame: () -> Unit,
) {
    // Exit Button (Door)
    Box(
        modifier = Modifier
            .size(40.dp)
            .background(Color.Gray, shape = RoundedCornerShape(8.dp))
            .clickable { onMenuClick() },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ExitToApp,
            contentDescription = stringResource(id = R.string.Exit),
            tint = Color.White,
            modifier = Modifier.size(24.dp)
        )
    }

    // Exit confirmation dialog
    if (showExitDialog) {
        AlertDialog(
            onDismissRequest = onDismissExitDialog,
            title = { Text(text = stringResource(id = R.string.EndGameTitle)) },
            text = { Text(text = stringResource(id = R.string.EndGameMessage)) },
            confirmButton = {
                TextButton(onClick = onConfirmExitGame) {
                    Text(text = stringResource(id = R.string.YesAction))
                }
            },
            dismissButton = {
                TextButton(onClick = onDismissExitDialog) {
                    Text(text = stringResource(id = R.string.NoAction))
                }
            }
        )
    }
}

/**
 * Stateful Header Buttons
 */
@Composable
fun SmartHeaderButtons(
    onExitGame: () -> Unit,
) {
    var showExitDialog by remember { mutableStateOf(false) }

    // Call stateless function
    HeaderButtons(
        showExitDialog = showExitDialog,
        onMenuClick = { showExitDialog = true },
        onDismissExitDialog = { showExitDialog = false },
        onConfirmExitGame = {
            showExitDialog = false
            onExitGame()
        }
    )
}

/**
 * Stateless fun for header on screens
 */
@Composable
fun ScreensHeaderArea(onExit: () -> Unit, modifier: Modifier, title: String) {
    Column {
        Row(
            modifier = modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = title,
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


/**
 * Utility to format seconds to hh:mm:ss
 */
private fun formatTime(seconds: Long): String {
    val h = seconds / 3600
    val m = (seconds % 3600) / 60
    val s = seconds % 60
    return String.format(Locale.getDefault(), "%02d:%02d:%02d", h, m, s)
}


@Preview(showBackground = true, widthDp = 600)
@Composable
fun GameHeaderAreaPortraitPreview() {
    MonopolyTheme {
        GameHeaderAreaPortrait(
            secondsRemaining = 900L,
            isTimerEnabled = true,
            currentPlayerName = "Player 1",
            currentPlayerId = 0,
            onExitGame = {}
        )
    }
}
