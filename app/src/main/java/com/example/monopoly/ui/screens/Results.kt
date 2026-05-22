package com.example.monopoly.ui.screens

import android.content.Intent
import android.content.res.Configuration
import android.icu.util.Calendar
import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.monopoly.R
import com.example.monopoly.ui.screens.ui.theme.MonopolyTheme
import java.text.SimpleDateFormat
import java.util.Locale
import androidx.core.net.toUri
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.runtime.getValue
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.platform.LocalConfiguration
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.monopoly.ui.viewmodel.ResultsViewModel
import com.example.monopoly.ui.viewmodel.LogViewModel


@Composable
fun ResultsScreen(
    viewModel: ResultsViewModel = viewModel(),
    onNewGame: () -> Unit,
    onExit: () -> Unit,
    logViewModel: LogViewModel,
    modifier: Modifier
) {
    val context = LocalContext.current
    val logScrollState = rememberScrollState()

    val calendar = Calendar.getInstance()
    val formatter = SimpleDateFormat("MMM dd, yyyy hh:mm:ss a", Locale.ENGLISH)
    val date = formatter.format(calendar.time)

    val isPortrait =
        LocalConfiguration.current.orientation == Configuration.ORIENTATION_PORTRAIT

    val logs by logViewModel.allLogs.collectAsStateWithLifecycle(initialValue = emptyList())
    // Get this game log
    val logInfo = logs.firstOrNull()?.let { log ->
        "${log.date} - ${log.winnerName} - ${log.durationMinutes}:\n${log.logLine}"
    } ?: ""

    val onSendEmail = {
        {
            val intent = Intent(Intent.ACTION_SENDTO).apply {
                data = "mailto:".toUri()
                putExtra(Intent.EXTRA_EMAIL, arrayOf(viewModel.email))
                putExtra(Intent.EXTRA_SUBJECT, "Log Monopoly - $date")
                putExtra(Intent.EXTRA_TEXT, logInfo)
            }
            context.startActivity(Intent.createChooser(intent, "Enviar log..."))
        }
    }

    if (isPortrait) {
        ResultsScreenPortrait(
            logInfo = logInfo,
            date = date,
            emailValue = viewModel.email,
            scrollState = logScrollState,
            onEmailChange = { viewModel.updateEmail(it) },
            onSendEmail = { onSendEmail() },
            onNewGame = onNewGame,
            onExit = onExit
        )
    } else {
        ResultsScreenLandscape(
            logInfo = logInfo,
            date = date,
            emailValue = viewModel.email,
            scrollState = logScrollState,
            onEmailChange = { viewModel.updateEmail(it) },
            onSendEmail = { onSendEmail() },
            onNewGame = onNewGame,
            onExit = onExit
        )
    }
}


@Composable
fun ResultsScreenPortrait(
    logInfo: String,
    date: String,
    emailValue: String,
    scrollState: ScrollState,
    onEmailChange: (String) -> Unit,
    onSendEmail: () -> Unit,
    onNewGame: () -> Unit,
    onExit: () -> Unit,
    modifier: Modifier = Modifier
) {
    val screenScrollState = rememberScrollState()
    Box(Modifier.fillMaxSize()) {
        // Background image
        Image(
            painter = painterResource(id = R.drawable.resultsbackgroundvertical),
            contentDescription = "Background",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // Content
        Column(
            modifier = modifier
                .fillMaxSize()
                .verticalScroll(screenScrollState)
        ) {
            // Header
            HeaderArea()

            // Date
            DateArea(date)
            Spacer(modifier = Modifier.height(20.dp))

            // Log values
            LogValuesArea(
                logInfo, Modifier
                    .heightIn(max = 250.dp) // Don't make it too big
                    .padding(horizontal = 16.dp, vertical = 12.dp) // External padding
                    .background(Color(0xFFF5F5F5), shape = RoundedCornerShape(8.dp))
                    .border(1.dp, Color.LightGray, shape = RoundedCornerShape(8.dp))
                    .verticalScroll(scrollState)
                    .padding(horizontal = 16.dp, vertical = 12.dp) // Internal paddin
            )
            Spacer(modifier = Modifier.height(20.dp))

            // Email
            EmailArea(emailValue, onEmailChange)
            Spacer(modifier = Modifier.height(20.dp))

            // Buttons
            ButtonsArea(onSendEmail, onNewGame, onExit)
        }
    }

}

@Composable
fun ResultsScreenLandscape(
    logInfo: String,
    date: String,
    emailValue: String,
    scrollState: ScrollState,
    onEmailChange: (String) -> Unit,
    onSendEmail: () -> Unit,
    onNewGame: () -> Unit,
    onExit: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(Modifier.fillMaxSize()) {
        // Background image
        Image(
            painter = painterResource(id = R.drawable.resultsbackgroundhz),
            contentDescription = "Background",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // Content
        Column(
            modifier = modifier.fillMaxSize()
        ) {
            // Header
            HeaderArea()

            Row(
                modifier = Modifier.fillMaxSize(),
                verticalAlignment = Alignment.Top
            ) {
                // Left Column
                Column(
                    modifier = Modifier
                        .weight(0.5f)
                        .fillMaxHeight()
                ) {
                    DateArea(date)
                    LogValuesArea(
                        logInfo, Modifier
                            .heightIn(max = 150.dp) // Don't make it too big
                            .padding(horizontal = 16.dp, vertical = 12.dp) // External padding
                            .background(Color(0xFFF5F5F5), shape = RoundedCornerShape(8.dp))
                            .border(1.dp, Color.LightGray, shape = RoundedCornerShape(8.dp))
                            .verticalScroll(scrollState)
                            .padding(horizontal = 16.dp, vertical = 12.dp) // Internal paddin
                    )
                }

                // Right column
                Column(
                    modifier = Modifier
                        .weight(0.5f)
                        .fillMaxHeight()
                ) {
                    EmailArea(emailValue, onEmailChange)
                    Spacer(modifier = Modifier.height(12.dp))
                    ButtonsArea(onSendEmail, onNewGame, onExit)
                }
            }
        }
    }
}

@Composable
fun HeaderArea() {
    // Title
    Text(
        text = stringResource(R.string.TittleGameResults),
        fontSize = 34.sp,
        textAlign = TextAlign.Center,
        fontWeight = FontWeight.Black,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 32.dp)
    )
}

@Composable
fun DateArea(date: String) {
    // Date and hour tittle
    Text(
        text = stringResource(R.string.GameResultDateTittle),
        fontSize = 18.sp,
        textAlign = TextAlign.Left,
        fontWeight = FontWeight.Black,
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp)
    )

    // Box with text
    Box(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 12.dp) // External padding
            .background(Color(0xFFF5F5F5), shape = RoundedCornerShape(8.dp))
            .border(1.dp, Color.LightGray, shape = RoundedCornerShape(8.dp))
            .padding(horizontal = 16.dp, vertical = 12.dp) // Internal padding

    ) {
        Text(
            text = date,
            fontSize = 16.sp,
            color = Color.Black
        )
    }
}

@Composable
fun LogValuesArea(logInfo: String, modifier: Modifier) {
    // Log values tittle
    Text(
        text = stringResource(R.string.GameResultLogTittle),
        fontSize = 18.sp,
        textAlign = TextAlign.Left,
        fontWeight = FontWeight.Black,
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp)
    )

    // Box with text
    Box(modifier = modifier) {
        Text(
            text = logInfo,
            fontSize = 16.sp,
            color = Color.Black
        )
    }
}

@Composable
fun EmailArea(emailValue: String, onEmailChange: (String) -> Unit) {
    // Email tittle
    Text(
        text = stringResource(R.string.GameResultEmailTittle),
        fontSize = 18.sp,
        fontWeight = FontWeight.Black,
        textAlign = TextAlign.Left,
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, top = 4.dp, bottom = 4.dp)
    )

    // Email input
    OutlinedTextField(
        value = emailValue,
        onValueChange = onEmailChange,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
        label = { Text(stringResource(id = R.string.TextFieldEnterTextEmail)) },
        placeholder = { Text(stringResource(id = R.string.DefaultEmail)) },
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White,
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp)

    )
}

// Use of column scope for buttons to align them in the center
@Composable
fun ColumnScope.ButtonsArea(onSendEmail: () -> Unit, onNewGame: () -> Unit, onExit: () -> Unit) {
    // Send email button
    Button(
        onClick = onSendEmail,
        modifier = Modifier
            .align(Alignment.CenterHorizontally)
            .padding(horizontal = 16.dp)
    ) {
        // Icon
        Icon(
            imageVector = Icons.Default.Email,
            contentDescription = null,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(10.dp))

        // Text
        Text(stringResource(R.string.ButtonSendEmail), fontSize = 18.sp)
    }
    Spacer(modifier = Modifier.height(4.dp))

    // New Game button
    Button(
        onClick = onNewGame,
        modifier = Modifier
            .align(Alignment.CenterHorizontally)
            .padding(horizontal = 16.dp)
    ) {
        // Icon
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = null,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(10.dp))

        // Text
        Text(stringResource(R.string.ButtonMenu), fontSize = 18.sp)
    }
    Spacer(modifier = Modifier.height(4.dp))

    // Exit button
    Button(
        onClick = onExit,
        modifier = Modifier
            .align(Alignment.CenterHorizontally)
            .padding(horizontal = 16.dp)
    ) {
        // Icon
        Icon(
            imageVector = Icons.Default.Close,
            contentDescription = null,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(10.dp))

        // Text
        Text(stringResource(R.string.ButtonExit), fontSize = 18.sp)
    }
}

@Preview(showBackground = true, heightDp = 900, widthDp = 600)
@Composable
fun ResultsPreview() {
    MonopolyTheme {
        ResultsScreenPortrait(
            logInfo = "infoinfoifnoinfoinfoifnoinfoinfoinfoifnoinfoinfoifnoinfo" +
                    "infoinfoifnoinfoinfoifnoinfoinfoinfoifnoinfoinfoifnoinfo" +
                    "infoinfoifnoinfoinfoifnoinfoinfoinfoifnoinfoinfoifnoinfo" +
                    "infoinfoifnoinfoinfoifnoinfoinfoinfoifnoinfoinfoifnoinfo",
            date = "Oct 25, 2023 10:30:00 AM",
            emailValue = "",
            scrollState = rememberScrollState(),
            onEmailChange = {},
            onSendEmail = {},
            onNewGame = {},
            onExit = {},
            modifier = Modifier
        )
    }
}