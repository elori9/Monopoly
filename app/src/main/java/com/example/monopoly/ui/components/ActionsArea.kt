package com.example.monopoly.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.monopoly.ui.theme.MonopolyTheme

/**
 * Component that displays the current player information and available actions.
 */
@Composable
fun ActionsArea(
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
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        // Money Section
        Text(
            text = "Money: $currentPlayerMoney$",
            fontSize = 36.sp,
            fontWeight = FontWeight.ExtraBold,
            color = Color.Black,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // Action Buttons Section - Vertical Layout
        val buttonModifier = Modifier
            .fillMaxWidth()
            .height(100.dp)

        ActionButton(
            text = "Buy Prop.",
            onClick = onBuyProperty,
            enabled = canBuyProperty,
            modifier = buttonModifier
        )

        ActionButton(
            text = "Buy House",
            onClick = onBuyHouse,
            enabled = canBuyHouse,
            modifier = buttonModifier
        )

        // Next Turn Button with Arrow Icon
        Button(
            onClick = onNextTurn,
            enabled = canNextTurn,
            modifier = buttonModifier,
            shape = RoundedCornerShape(32.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF03A9F4),
                disabledContainerColor = Color.LightGray
            ),
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Next Turn",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White
                )
                Spacer(modifier = Modifier.width(16.dp))
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = null,
                    modifier = Modifier.size(48.dp),
                    tint = Color.White
                )
            }
        }
    }
}

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
        shape = RoundedCornerShape(32.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF03A9F4),
            disabledContainerColor = Color.LightGray
        ),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
    ) {
        Text(
            text = text,
            fontSize = 32.sp,
            fontWeight = FontWeight.ExtraBold,
            color = Color.White
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ActionsAreaPreview() {
    MonopolyTheme {
        Box(modifier = Modifier.height(500.dp).width(400.dp)) {
            ActionsArea(
                currentPlayerMoney = 500,
                onBuyProperty = {},
                onBuyHouse = {},
                onNextTurn = {},
                canBuyProperty = true,
                canBuyHouse = true,
                canNextTurn = true
            )
        }
    }
}
