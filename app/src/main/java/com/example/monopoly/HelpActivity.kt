package com.example.monopoly

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.monopoly.ui.theme.MonopolyTheme

class HelpActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MonopolyTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    HelpScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

/**
 * Stateful function
 */
@Composable
fun HelpScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current

    HelpContent(
        onBackClick = {
            val intent = Intent(context, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            context.startActivity(intent)
        },
        modifier = modifier
    )
}

/**
 * Stateless function
 */
@Composable
fun HelpContent(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Header
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            // Back button
            IconButton(onClick = onBackClick) {
                Icon(
                    // This icon no need to finish context, it does it solo
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back"
                )
            }
            // Tittle
            Text(
                text = stringResource(R.string.HowPlay),
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 8.dp)
            )
        }

        HorizontalDivider(thickness = 2.dp, modifier = Modifier.padding(vertical = 16.dp))

        // Body
        Text(
            // Instructions
            text = "",
            fontSize = 18.sp,
            lineHeight = 28.sp,
            modifier = Modifier.padding(horizontal = 8.dp)
        )
        // TODO: INCLUDE ICONS + IMAGES
    }
}