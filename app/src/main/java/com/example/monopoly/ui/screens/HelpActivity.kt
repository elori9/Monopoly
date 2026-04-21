package com.example.monopoly.ui.screens

import android.app.SearchManager
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
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
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.monopoly.R
import com.example.monopoly.ui.theme.MonopolyTheme
import coil.compose.AsyncImage

object Constants {
    const val GITHUB_LINK = "https://github.com/elori9/Monopoly"
}

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
            onClickButton = {
                Toast.makeText(
                    context,
                    context.getString(R.string.Searching),
                    Toast.LENGTH_LONG
                ).show()
                val intent = Intent(Intent.ACTION_WEB_SEARCH)
                intent.putExtra(SearchManager.QUERY, Constants.GITHUB_LINK)
                context.startActivity(intent)
            }
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
    onClickButton: () -> Unit,
    modifier: Modifier = Modifier
) {

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        item {
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
        }

        item {
            HorizontalDivider(thickness = 2.dp, modifier = Modifier.padding(vertical = 16.dp))
        }

        item {

            // Body
            Text(
                text = stringResource(R.string.Instructions),
                fontSize = 16.sp,
                lineHeight = 28.sp,
                textAlign = TextAlign.Justify,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(32.dp))
        }

        item {
            // Pieces
            SectionTitle(stringResource(R.string.PiecesInfo))
            ShowPieces()
            Spacer(modifier = Modifier.height(24.dp))
        }

        item {
            // Money
            SectionTitle(stringResource(R.string.EconomyInfo))
            ShowEconomy()
            Spacer(modifier = Modifier.height(24.dp))
        }

        item {
            // Show special boxes
            SectionTitle(stringResource(R.string.SpecialBoxesInfo))
            ShowSpecialBoxes(onBoxClick)
            Spacer(modifier = Modifier.height(24.dp))
        }

        item {
            // Show boxes (Passing the click callback)
            SectionTitle(stringResource(R.string.BoxesInfo))
            ShowBoxes(onBoxClick)
            Spacer(modifier = Modifier.height(24.dp))
        }

        item {
            // Show additional info
            SectionTitle(stringResource(R.string.AdditionalInfoTitle))
            ShowAdditionalInfo(onClickButton)
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

/**
 * Tittles more beautiful
 */
@Composable
fun SectionTitle(title: String) {
    Text(
        text = title,
        fontSize = 22.sp,
        fontWeight = FontWeight.Bold,
        color = Color(0xFF03A9F4),
        modifier = Modifier.padding(start = 8.dp, bottom = 12.dp)
    )
}

/**
 * Show the pieces
 */
@Composable
fun ShowPieces() {
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
fun ShowEconomy() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        TokenPreview(imageId = R.drawable.icon5, label = stringResource(R.string.MoneyInfo))
        TokenPreview(imageId = R.drawable.icon6, label = stringResource(R.string.HouseInfo))
    }
}


/**
 * Show special boxes
 */
@Composable
fun ShowSpecialBoxes(onBoxClick: (Int) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        TokenPreview(
            imageId = R.drawable.icon7,
            stringResource(R.string.StartLabel),
            onClick = { onBoxClick(R.drawable.icon7) })
        TokenPreview(
            imageId = R.drawable.icon8,
            stringResource(R.string.JailLabel),
            onClick = { onBoxClick(R.drawable.icon8) })
        TokenPreview(
            imageId = R.drawable.icon9,
            stringResource(R.string.LuckLabel),
            onClick = { onBoxClick(R.drawable.icon9) })
        TokenPreview(
            imageId = R.drawable.icon10,
            stringResource(R.string.FeeLabel),
            onClick = { onBoxClick(R.drawable.icon10) })
    }
}

/**
 * Show the boxes
 */
@Composable
fun ShowBoxes(onBoxClick: (Int) -> Unit) {
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
        AsyncImage(
            model = imageId,
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

@Composable
fun ShowAdditionalInfo(onClick: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(id = R.string.AdditionalInfoText),
            modifier = Modifier
                .weight(1f)
                .padding(start = 8.dp, end = 8.dp)

        )
        ElevatedButton(
            modifier = Modifier.padding(vertical = 10.dp),
            elevation = ButtonDefaults.elevatedButtonElevation(5.dp),
            onClick = onClick
        ) {
            Icon(
                painter = painterResource(id = R.drawable.icon19),
                contentDescription = "Github repo",
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

