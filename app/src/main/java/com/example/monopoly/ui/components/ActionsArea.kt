package com.example.monopoly.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.monopoly.R
import com.example.monopoly.ui.theme.MonopolyTheme

/**
 * Stateful function
 */
@Composable
fun ActionsArea(
    currentPlayerMoney: Int,
    ownedPropertyIcons: List<Int>,
    onBuyProperty: () -> Unit,
    onBuyHouse: () -> Unit,
    onNextTurn: () -> Unit,
    canBuyProperty: Boolean,
    canBuyHouse: Boolean,
    canNextTurn: Boolean,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()
    var expandedImageRes by remember { mutableStateOf<Int?>(null) }

    Box(modifier = modifier.fillMaxWidth()) {
        ActionsAreaContent(
            currentPlayerMoney = currentPlayerMoney,
            ownedPropertyIcons = ownedPropertyIcons,
            scrollState = scrollState,
            onBuyProperty = onBuyProperty,
            onBuyHouse = onBuyHouse,
            onNextTurn = onNextTurn,
            canBuyProperty = canBuyProperty,
            canBuyHouse = canBuyHouse,
            canNextTurn = canNextTurn,
            onCardClick = { resId -> expandedImageRes = resId }
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
                        .clickable { expandedImageRes = null },
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
fun ActionsAreaContent(
    currentPlayerMoney: Int,
    ownedPropertyIcons: List<Int>,
    scrollState: ScrollState,
    onBuyProperty: () -> Unit,
    onBuyHouse: () -> Unit,
    onNextTurn: () -> Unit,
    canBuyProperty: Boolean,
    canBuyHouse: Boolean,
    canNextTurn: Boolean,
    onCardClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(250.dp)
            .background(Color(0xFFCECED2)) // Grey background
            .border(2.dp, Color.Black)
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Left Side: Scrollable Property Cards Area
        ShowOwnedProperties(
            ownedPropertyIcons = ownedPropertyIcons,
            scrollState = scrollState,
            onCardClick = onCardClick,
            modifier = Modifier.weight(1f)
        )

        Spacer(modifier = Modifier.width(16.dp))

        // Right Side: Money and Actions
        ShowPlayerActions(
            currentPlayerMoney = currentPlayerMoney,
            onBuyProperty = onBuyProperty,
            onBuyHouse = onBuyHouse,
            onNextTurn = onNextTurn,
            canBuyProperty = canBuyProperty,
            canBuyHouse = canBuyHouse,
            canNextTurn = canNextTurn,
            modifier = Modifier.width(IntrinsicSize.Max)
        )
    }
}

/**
 * Show the owned properties in a scrollable row
 */
@Composable
fun ShowOwnedProperties(
    ownedPropertyIcons: List<Int>,
    scrollState: ScrollState,
    onCardClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxHeight()
            .horizontalScroll(scrollState),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (ownedPropertyIcons.isEmpty()) {
            Text(
                text = stringResource(id = R.string.NoProperties),
                color = Color.DarkGray,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        } else {
            ownedPropertyIcons.forEach { iconRes ->
                Image(
                    painter = painterResource(id = iconRes),
                    contentDescription = "Property Card",
                    modifier = Modifier
                        .fillMaxHeight()
                        .aspectRatio(0.7f)
                        .clickable { onCardClick(iconRes) },
                    contentScale = ContentScale.Fit
                )
            }
        }
    }
}

/**
 * Show the player money and action buttons
 */
@Composable
fun ShowPlayerActions(
    currentPlayerMoney: Int,
    onBuyProperty: () -> Unit,
    onBuyHouse: () -> Unit,
    onNextTurn: () -> Unit,
    canBuyProperty: Boolean,
    canBuyHouse: Boolean,
    canNextTurn: Boolean,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxHeight(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically)
    ) {
        // Money Section
        Text(
            text = stringResource(id = R.string.MoneyLabel, currentPlayerMoney),
            fontSize = 24.sp,
            fontWeight = FontWeight.ExtraBold,
            color = Color.Black
        )

        // Action Buttons Section
        val buttonModifier = Modifier
            .fillMaxWidth()
            .height(55.dp)

        ActionButton(
            text = stringResource(id = R.string.BuyPropAction),
            onClick = onBuyProperty,
            enabled = canBuyProperty,
            modifier = buttonModifier
        )

        ActionButton(
            text = stringResource(id = R.string.BuyHouseAction),
            onClick = onBuyHouse,
            enabled = canBuyHouse,
            modifier = buttonModifier
        )

        // Next Turn Button
        Button(
            onClick = onNextTurn,
            enabled = canNextTurn,
            modifier = buttonModifier,
            shape = RoundedCornerShape(20.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF03A9F4),
                disabledContainerColor = Color.LightGray
            ),
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = stringResource(id = R.string.NextTurnAction),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White
                )
                Spacer(modifier = Modifier.width(6.dp))
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp),
                    tint = Color.White
                )
            }
        }
    }
}

/**
 * Draw an action button
 */
@Composable
fun ActionButton(
    text: String,
    onClick: () -> Unit,
    enabled: Boolean,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier,
        shape = RoundedCornerShape(20.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF03A9F4),
            disabledContainerColor = Color.LightGray
        ),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
    ) {
        Text(
            text = text,
            fontSize = 16.sp,
            fontWeight = FontWeight.ExtraBold,
            color = Color.White
        )
    }
}

@Preview(showBackground = true, widthDp = 800)
@Composable
fun ActionsAreaPreview() {
    MonopolyTheme {
        ActionsArea(
            currentPlayerMoney = 500,
            ownedPropertyIcons = listOf(R.drawable.icon15, R.drawable.icon17, R.drawable.icon14),
            onBuyProperty = {},
            onBuyHouse = {},
            onNextTurn = {},
            canBuyProperty = true,
            canBuyHouse = true,
            canNextTurn = true
        )
    }
}
