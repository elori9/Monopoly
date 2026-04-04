package com.example.monopoly.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.monopoly.R
import com.example.monopoly.ui.theme.MonopolyTheme
import java.util.Locale


/**
 * Stateless function: Purely visual representation.
 */
@Composable
fun HeaderAreaPortrait(
    secondsRemaining: Long,
    isTimerEnabled: Boolean,
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
        // Spacer to keep the timer centered (adjusted to button size)
        Spacer(modifier = Modifier.width(48.dp))

        // Timer
        GameTimer(
            secondsRemaining = secondsRemaining,
            isTimerEnabled = isTimerEnabled
        )
        // Buttons
        SmartHeaderButtons(
            onExitGame = onExitGame
        )
    }
}

/**
 * Stateless function: draws te timer
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
            fontSize = 20.sp,
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
            .size(20.dp)
            .background(Color.Gray, shape = RoundedCornerShape(8.dp))
            .clickable { onMenuClick() },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ExitToApp,
            contentDescription = stringResource(id = R.string.Exit),
            tint = Color.White,
            modifier = Modifier.size(15.dp)
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
fun HeaderAreaPortraitPreview() {
    MonopolyTheme {
        HeaderAreaPortrait(
            secondsRemaining = 900L,
            isTimerEnabled = true,
            onExitGame = {}
        )
    }
}
