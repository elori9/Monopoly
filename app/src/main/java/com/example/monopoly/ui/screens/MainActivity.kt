package com.example.monopoly.ui.screens

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.monopoly.R
import com.example.monopoly.ui.theme.MonopolyTheme
import com.example.monopoly.ui.viewmodel.MainActivityViewModel
import kotlin.system.exitProcess

class MainActivity : ComponentActivity() {
    private val viewModel: MainActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MonopolyTheme {
                val context = LocalContext.current
                val isPortrait =
                    LocalConfiguration.current.orientation == Configuration.ORIENTATION_PORTRAIT

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    if (isPortrait) {
                        MainMenuScreenPortrait(
                            modifier = Modifier.padding(innerPadding),
                            onNewGameClick = {
                                startActivity(Intent(context, ConfigActivity::class.java))
                            },
                            onHelpClick = {
                                startActivity(Intent(context, HelpActivity::class.java))
                            },
                            onExitClick = {
                                exitProcess(0)
                            })
                    } else {
                        MainMenuScreenLandscape(
                            modifier = Modifier.padding(innerPadding),
                            onNewGameClick = {
                                startActivity(Intent(context, ConfigActivity::class.java))
                            },
                            onHelpClick = {
                                startActivity(Intent(context, HelpActivity::class.java))
                            },
                            onExitClick = {
                                exitProcess(0)
                            })
                    }
                }
            }
        }
    }
}

/**
 * Portrait menu stateless
 */
@Composable
fun MainMenuScreenPortrait(
    modifier: Modifier = Modifier,
    onNewGameClick: () -> Unit,
    onHelpClick: () -> Unit,
    onExitClick: () -> Unit
) {
    Box(modifier = modifier.fillMaxSize()) {
        // Background
        Image(
            painter = painterResource(id = R.drawable.verticalbackground),
            contentDescription = "Background",
            contentScale = ContentScale.Crop, // Fill on the screen
            modifier = Modifier.fillMaxSize()
        )

        Column(
            modifier = modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.app_name),
                fontSize = 64.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 30.dp),
                style = TextStyle( // Put color white with black border
                    color = Color.White,
                    shadow = Shadow(
                        color = Color.Black,
                        offset = Offset(4f, 4f),
                        blurRadius = 8f
                    )
                ),
            )
            val buttonModifier = Modifier
                .width(250.dp)
                .height(80.dp)
                .padding(vertical = 8.dp)
            Buttons(
                modifier = buttonModifier,
                onNewGameClick = onNewGameClick,
                onHelpClick = onHelpClick,
                onExitClick = onExitClick
            )
        }
    }
}

/**
 * Landscape main menu stateless
 */
@Composable
fun MainMenuScreenLandscape(
    modifier: Modifier = Modifier,
    onNewGameClick: () -> Unit,
    onHelpClick: () -> Unit,
    onExitClick: () -> Unit
) {
    Box(modifier = modifier.fillMaxSize()) {
        // Background
        Image(
            painter = painterResource(id = R.drawable.horizaontalbackground),
            contentDescription = "Background",
            contentScale = ContentScale.Crop, // Fill on the screen
            modifier = Modifier.fillMaxSize()
        )

        // Top
        Column(
            modifier = modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.app_name),
                fontSize = 64.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 30.dp),
                style = TextStyle( // Put color white with black border
                    color = Color.White,
                    shadow = Shadow(
                        color = Color.Black,
                        offset = Offset(4f, 4f),
                        blurRadius = 8f
                    )
                ),
            )
            val buttonModifier = Modifier
                .width(300.dp)
                .height(60.dp)
                .padding(vertical = 8.dp)
            Buttons(
                modifier = buttonModifier,
                onNewGameClick = onNewGameClick,
                onHelpClick = onHelpClick,
                onExitClick = onExitClick
            )
        }
    }
}

/**
 * Buttons
 */
@Composable
fun Buttons(
    modifier: Modifier,
    onNewGameClick: () -> Unit,
    onHelpClick: () -> Unit,
    onExitClick: () -> Unit
) {

    Button(
        onClick = onNewGameClick,
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF03A9F4))
    ) {
        Text(
            text = stringResource(R.string.NewGame),
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )
    }

    Button(
        onClick = onHelpClick,
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF03A9F4))
    ) {
        Text(
            text = stringResource(R.string.Help),
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )
    }

    Button(
        onClick = onExitClick,
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF03A9F4))
    ) {
        Text(
            text = stringResource(R.string.Exit),
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Preview(showBackground = true, widthDp = 300, heightDp = 600)
@Composable
fun MainMenuPreview() {
    MonopolyTheme {
        MainMenuScreenPortrait(
            onNewGameClick = {},
            onHelpClick = {},
            onExitClick = {}
        )
    }
}
