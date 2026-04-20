package com.example.monopoly.ui.screens

import android.content.Intent
import android.content.res.Configuration
import android.icu.util.Calendar
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
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
import com.example.monopoly.ui.viewmodel.ResultsViewModel
import java.text.SimpleDateFormat
import java.util.Locale
import kotlin.system.exitProcess
import androidx.core.net.toUri
import androidx.compose.material3.Icon

class Results : ComponentActivity() {
    private val viewModel: ResultsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val gameInfo = intent?.getStringExtra("INFO") ?: "No game log"

        val calendar = Calendar.getInstance()
        val formatter = SimpleDateFormat("MMM dd, yyyy hh:mm:ss a", Locale.ENGLISH)
        val date = formatter.format(calendar.time)

        setContent {
            MonopolyTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    ResultsScreen(
                        logInfo = gameInfo,
                        date = date,
                        emailValue = viewModel.email,
                        onEmailChange = { viewModel.updateEmail(it) },
                        onSendEmail = {
                            val intent = Intent(Intent.ACTION_SENDTO).apply {
                                data = "mailto:".toUri()
                                putExtra(Intent.EXTRA_EMAIL, arrayOf(viewModel.email))
                                putExtra(Intent.EXTRA_SUBJECT, "Log Monopoly - $date")
                                putExtra(Intent.EXTRA_TEXT, gameInfo)
                            }
                            startActivity(Intent.createChooser(intent, "Enviar log..."))
                        },
                        onNewGame = {
                            val intent = Intent(this, ConfigActivity::class.java)
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
    logInfo: String,
    date: String,
    emailValue: String,
    onEmailChange: (String) -> Unit,
    onSendEmail: () -> Unit,
    onNewGame: () -> Unit,
    onExit: () -> Unit,
    modifier: Modifier
) {
    val logScrollState = rememberScrollState()

    val isPortrait =
        LocalContext.current.resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT

    if (isPortrait) {
        ResultsScreenPortrait(
            logInfo = logInfo,
            date = date,
            emailValue = emailValue,
            scrollState = logScrollState,
            onEmailChange = onEmailChange,
            onSendEmail = onSendEmail,
            onNewGame = onNewGame,
            onExit = onExit
        )
    } else {
        ResultsScreenLandscape(
            logInfo = logInfo,
            date = date,
            emailValue = emailValue,
            scrollState = logScrollState,
            onEmailChange = onEmailChange,
            onSendEmail = onSendEmail,
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
    Column(
        modifier = modifier
            .fillMaxSize()
    ) {
        // Header
        HeaderArea()
        Spacer(modifier = Modifier.height(20.dp))

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
                ButtonsArea(onSendEmail, onNewGame, onExit)
            }
        }
    }
}

@Composable
fun HeaderArea() {
    // Title
    Text(
        text = stringResource(R.string.TittleGameResults),
        fontSize = 24.sp,
        textAlign = TextAlign.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 16.dp)
    )
}

@Composable
fun DateArea(date: String) {
    // Date and hour tittle
    Text(
        text = stringResource(R.string.GameResultDateTittle),
        fontSize = 18.sp,
        textAlign = TextAlign.Left,
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
        textAlign = TextAlign.Left,
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp)
    )

    // Email input
    OutlinedTextField(
        value = emailValue,
        onValueChange = onEmailChange,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
        label = { Text(stringResource(id = R.string.TextFieldEnterTextEmail)) },
        placeholder = { Text(stringResource(id = R.string.DefaultEmail)) },
        modifier = Modifier.padding(16.dp)
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
    Spacer(modifier = Modifier.height(10.dp))

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
        Text(stringResource(R.string.ButtonNewGame), fontSize = 18.sp)
    }
    Spacer(modifier = Modifier.height(10.dp))

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

@Preview(showBackground = true, heightDp =350, widthDp = 700)
@Composable
fun ResultsPreview() {
    MonopolyTheme {
        ResultsScreen(
            logInfo = "infoinfoinfoinfoinfoinfoinfoinfoinfoinfoinfoinfoinfoinfo" +
                    "infoinfoinfoinfoinfoinfoinfoinfoinfoinfoinfoinfoinfoinfo" +
                    "infoinfoinfoinfoinfoinfoinfoinfoinfoinfoinfoinfoinfoinfo" +
                    "infoinfoinfoinfoinfoinfoinfoinfoinfoinfoinfoinfoinfoinfo" +
                    "infoinfoinfoinfoinfoinfoinfoinfoinfoinfoinfoinfoinfoinfo" +
                    "infoinfoinfoinfoinfoinfoinfoinfoinfoinfoinfoinfoinfoinfo" +
                    "infoinfoinfoinfoinfoinfoinfoinfoinfoinfoinfoinfoinfoinfo" +
                    "infoinfoinfoinfoinfoinfoinfoinfoinfoinfoinfoinfoinfoinfo" +
                    "infoinfoinfoinfoinfoinfoinfoinfoinfoinfoinfoinfoinfoinfo" +
                    "infoinfoinfoinfoinfoinfoinfoinfoinfoinfoinfoinfoinfoinfo",
            emailValue = "abc@example.com",
            date = "Apr 11, 2026 21:30:00 PM",
            onEmailChange = {},
            onNewGame = {},
            onSendEmail = {},
            onExit = {},
            modifier = Modifier
        )
    }
}