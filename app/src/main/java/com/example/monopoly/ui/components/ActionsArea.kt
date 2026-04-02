package com.example.monopoly.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
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
        modifier = modifier
    )
}

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
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Left Side: Scrollable Property Cards Area
        Row(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .horizontalScroll(scrollState),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (ownedPropertyIcons.isEmpty()) {
                Text(
                    text = stringResource(id = R.string.NoProperties),
                    color = Color.Gray,
                    fontSize = 14.sp
                )
            } else {
                ownedPropertyIcons.forEach { iconRes ->
                    Image(
                        painter = painterResource(id = iconRes),
                        contentDescription = "Property Card",
                        modifier = Modifier
                            .fillMaxHeight()
                            .aspectRatio(0.7f),
                        contentScale = ContentScale.Fit
                    )
                }
            }
        }

        Spacer(modifier = Modifier.width(24.dp))

        // Right Side: Money and Actions
        Column(
            modifier = Modifier
                .width(IntrinsicSize.Max),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterVertically)
        ) {
            Text(
                text = stringResource(id = R.string.MoneyLabel, currentPlayerMoney),
                fontSize = 28.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color.Black
            )

            val buttonModifier = Modifier
                .fillMaxWidth()
                .height(65.dp)

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

            Button(
                onClick = onNextTurn,
                enabled = canNextTurn,
                modifier = buttonModifier,
                shape = RoundedCornerShape(24.dp),
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
                        fontSize = 20.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = null,
                        modifier = Modifier.size(28.dp),
                        tint = Color.White
                    )
                }
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
        shape = RoundedCornerShape(24.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF03A9F4),
            disabledContainerColor = Color.LightGray
        ),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
    ) {
        Text(
            text = text,
            fontSize = 20.sp,
            fontWeight = FontWeight.ExtraBold,
            color = Color.White
        )
    }
}

/**
 * For designing
 */
@Preview(showBackground = true, widthDp = 800, heightDp = 300)
@Composable
fun ActionsAreaPreview() {
    MonopolyTheme {
        ActionsArea(
            currentPlayerMoney = 500,
            ownedPropertyIcons = listOf(R.drawable.icon15, R.drawable.icon13),
            onBuyProperty = {},
            onBuyHouse = {},
            onNextTurn = {},
            canBuyProperty = true,
            canBuyHouse = true,
            canNextTurn = true
        )
    }
}
