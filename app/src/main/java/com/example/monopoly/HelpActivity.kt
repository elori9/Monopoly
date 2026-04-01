package com.example.monopoly

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
    var expandedImageRes by remember { mutableStateOf<Int?>(null) }

    Box(modifier = modifier.fillMaxSize()) {
        HelpContent(
            onBackClick = {
                val intent = Intent(context, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                context.startActivity(intent)
            },
            onBoxClick = { resId -> expandedImageRes = resId },
            modifier = Modifier.fillMaxSize()
        )

        // Overlay for enlarged image
        AnimatedVisibility(
            visible = expandedImageRes != null,
            enter = fadeIn() + scaleIn(),
            exit = fadeOut() + scaleOut()
        ) {
            expandedImageRes?.let { resId ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.8f))
                        .clickable { expandedImageRes = null }, // Returns to normal size when click
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = resId),
                        contentDescription = null,
                        modifier = Modifier.size(300.dp) // Big size at the center
                    )
                }
            }
        }
    }
}

/**
 * Stateless function
 */
@Composable
fun HelpContent(
    onBackClick: () -> Unit,
    onBoxClick: (Int) -> Unit,
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
            text = stringResource(R.string.Instructions),
            fontSize = 16.sp,
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

        // Show boxes (Passing the click callback)
        ShowBoxes(onBoxClick)
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
        TokenPreview(imageId = R.drawable.icon7, stringResource(R.string.StartLabel))
        TokenPreview(imageId = R.drawable.icon8, stringResource(R.string.JailLabel))
        TokenPreview(imageId = R.drawable.icon9, stringResource(R.string.LuckLabel))
        TokenPreview(imageId = R.drawable.icon10, stringResource(R.string.FeeLabel))
    }
}

/**
 * Show the boxes
 */
@Composable
fun ShowBoxes(onBoxClick: (Int) -> Unit) {
    Text(
        text = stringResource(R.string.BoxesInfo),
        fontSize = 20.sp,
        modifier = Modifier.padding(start = 20.dp, end = 2.dp)
    )
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        TokenPreview(imageId = R.drawable.icon11, "", onClick = { onBoxClick(R.drawable.icon11) })
        TokenPreview(imageId = R.drawable.icon12, "", onClick = { onBoxClick(R.drawable.icon12) })
        TokenPreview(imageId = R.drawable.icon13, "", onClick = { onBoxClick(R.drawable.icon13) })
        TokenPreview(imageId = R.drawable.icon14, "", onClick = { onBoxClick(R.drawable.icon14) })
    }
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        TokenPreview(imageId = R.drawable.icon15, "", onClick = { onBoxClick(R.drawable.icon15) })
        TokenPreview(imageId = R.drawable.icon16, "", onClick = { onBoxClick(R.drawable.icon16) })
        TokenPreview(imageId = R.drawable.icon17, "", onClick = { onBoxClick(R.drawable.icon17) })
        TokenPreview(imageId = R.drawable.icon18, "", onClick = { onBoxClick(R.drawable.icon18) })
    }
}

/**
 * Shows the image
 */
@Composable
fun TokenPreview(imageId: Int, label: String, onClick: (() -> Unit)? = null) {
    val modifier = if (onClick != null) {
        Modifier.clickable(onClick = onClick)
    } else {
        Modifier
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.padding(8.dp)
    ) {
        Image(
            painter = painterResource(id = imageId),
            contentDescription = label,
            modifier = Modifier.size(64.dp)
        )
        if (label.isNotEmpty()) {
            Text(
                text = label,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}
