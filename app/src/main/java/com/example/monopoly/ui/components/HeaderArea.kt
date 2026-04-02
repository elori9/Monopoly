package com.example.monopoly.ui.components

import androidx.compose.foundation.background
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

@Composable
fun HeaderArea(
    secondsRemaining: Long,
    onExitGame: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showExitDialog by remember { mutableStateOf(false) }

    HeaderAreaContent(
        secondsRemaining = secondsRemaining,
        showExitDialog = showExitDialog,
        onMenuClick = { showExitDialog = true },
        onDismissExitDialog = { showExitDialog = false },
        onConfirmExitGame = {
            showExitDialog = false
            onExitGame()
        },
        modifier = modifier
    )
}

@Composable
fun HeaderAreaContent(
    secondsRemaining: Long,
    showExitDialog: Boolean,
    onMenuClick: () -> Unit,
    onDismissExitDialog: () -> Unit,
    onConfirmExitGame: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // Space to center the timer
        Spacer(modifier = Modifier.width(48.dp))

        // Timer Section
        if (secondsRemaining > 0) {
            Text(
                text = stringResource(id = R.string.TimeLabel, formatTime(secondsRemaining)),
                fontSize = 32.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color.Black
            )
        } else {
            Spacer(modifier = Modifier.weight(1f))
        }

        // Exit Button with Door Icon
        IconButton(
            onClick = onMenuClick,
            modifier = Modifier
                .size(48.dp)
                .background(Color.Gray, shape = RoundedCornerShape(8.dp))
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                contentDescription = stringResource(id = R.string.Exit),
                tint = Color.White
            )
        }
    }

    // Exit Confirmation Dialog
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
 * Format seconds into hh:mm:ss
 */
private fun formatTime(seconds: Long): String {
    val h = seconds / 3600
    val m = (seconds % 3600) / 60
    val s = seconds % 60
    return String.format(Locale.getDefault(), "%02d:%02d:%02d", h, m, s)
}

/**
 * For designing
 */
@Preview(showBackground = true, widthDp = 600)
@Composable
fun HeaderAreaPreview() {
    MonopolyTheme {
        HeaderArea(
            secondsRemaining = 3661, // 01:01:01
            onExitGame = {}
        )
    }
}
