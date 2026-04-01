package com.example.monopoly

import android.content.Intent
import android.graphics.ImageDecoder
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
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
    // Scroll
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(scrollState)
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
        // Pieces
        ShowPieces()

        // Money
        ShowMoney()

        // Houses
        ShowHouse()

        // Show special boxes
        ShowSpecialBoxes()

        // Show boxes
        ShowBoxes()

    }

}

/**
 * Show the pieces
 */
@Composable
fun ShowPieces() {
    Text(
        text = stringResource(R.string.PiecesInfo),
        fontSize = 20.sp,
        modifier = Modifier.padding(start = 8.dp, end = 2.dp)
    )
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        TokenPreview(imageId = R.drawable.icon1, stringResource(R.string.LlamaLabel))
        TokenPreview(imageId = R.drawable.icon2, stringResource(R.string.PickaxeLabel))
        TokenPreview(imageId = R.drawable.icon3, stringResource(R.string.ChestLabel))
        TokenPreview(imageId = R.drawable.icon4, stringResource(R.string.ShieldLabel))
    }
}

/**
 * Show Money
 */
@Composable
fun ShowMoney() {
    Text(
        text = stringResource(R.string.MoneyInfo),
        fontSize = 20.sp,
        modifier = Modifier.padding(start = 20.dp, end = 2.dp)
    )
    TokenPreview(imageId = R.drawable.icon5, label = "")
}

/**
 * Show the house
 */
@Composable
fun ShowHouse() {
    Text(
        text = stringResource(R.string.HouseInfo),
        fontSize = 20.sp,
        modifier = Modifier.padding(start = 20.dp, end = 2.dp)
    )
    TokenPreview(imageId = R.drawable.icon6, label = "")
}

/**
 * Show special boxes
 */
@Composable
fun ShowSpecialBoxes() {
    Text(
        text = stringResource(R.string.SpecialBoxesInfo),
        fontSize = 20.sp,
        modifier = Modifier.padding(start = 20.dp, end = 2.dp)
    )
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        TokenPreview(imageId = R.drawable.icon7, label = stringResource(R.string.StartLabel))
        TokenPreview(imageId = R.drawable.icon8, label = stringResource(R.string.JailLabel))
        TokenPreview(imageId = R.drawable.icon9, label = stringResource(R.string.LuckLabel))
        TokenPreview(imageId = R.drawable.icon10, label = stringResource(R.string.FeeLabel))

    }

}

/**
 * Show the boxes
 */
@Composable
fun ShowBoxes() {
    Text(
        text = stringResource(R.string.BoxesInfo),
        fontSize = 20.sp,
        modifier = Modifier.padding(start = 20.dp, end = 2.dp)
    )
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        TokenPreview(imageId = R.drawable.icon11, label = "")
        TokenPreview(imageId = R.drawable.icon12, label = "")
        TokenPreview(imageId = R.drawable.icon13, label = "")
        TokenPreview(imageId = R.drawable.icon14, label = "")

    }
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        TokenPreview(imageId = R.drawable.icon15, label = "")
        TokenPreview(imageId = R.drawable.icon16, label = "")
        TokenPreview(imageId = R.drawable.icon17, label = "")
        TokenPreview(imageId = R.drawable.icon18, label = "")
    }
}

/**
 * Shows the image
 */
@Composable
fun TokenPreview(imageId: Int, label: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = imageId),
            contentDescription = label,
            modifier = Modifier.size(64.dp)
        )
        Text(
            text = label,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}