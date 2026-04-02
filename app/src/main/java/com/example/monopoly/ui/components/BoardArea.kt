package com.example.monopoly.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.monopoly.ui.theme.MonopolyTheme
import game.model.box.GameBox
import game.model.box.Start
import com.example.monopoly.R
import game.model.box.BoxName
import game.model.box.Fee
import game.model.box.Jail
import game.model.box.Property

/**
 * Draw the board
 */
@Composable
fun BoardArea(
    topGameBoxes: List<GameBox>,
    bottomGameBoxes: List<GameBox>,
    leftGameBoxes: List<GameBox>,
    rightGameBoxes: List<GameBox>,
    centerContent: @Composable () -> Unit // Save space for dices in the middle
) {
    Column(modifier = Modifier.fillMaxWidth()) {

        // Top
        Row(modifier = Modifier
            .fillMaxWidth()
            .weight(0.25f)) {
            // Draw all boxes
            topGameBoxes.forEach { box ->
                DrawBox(
                    box,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                )
            }
        }

        // Middle
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.5f)
        ) {

            // Left boxes
            Column(
                modifier = Modifier
                    .weight(0.25f)
                    .fillMaxHeight()
            ) {
                // Draw all boxes
                leftGameBoxes.forEach { box ->
                    DrawBox(
                        box,
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                    )
                }
            }

            // Middle space for dice
            Box(
                modifier = Modifier
                    .weight(0.5f)
                    .fillMaxHeight()
            ) {
                centerContent()
            }

            // Right boxes
            Column(
                modifier = Modifier
                    .weight(0.25f)
                    .fillMaxHeight()
            ) {
                // Draw all boxes
                rightGameBoxes.forEach { box ->
                    DrawBox(
                        box,
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                    )
                }
            }
        }

        // Bottom
        Row(modifier = Modifier
            .fillMaxWidth()
            .weight(0.25f)) {
            // Draw all boxes
            bottomGameBoxes.forEach { box ->
                DrawBox(
                    box,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                )
            }
        }
    }
}


/**
 * Draw the box
 */
@Composable
fun DrawBox(gameBox: GameBox, modifier: Modifier = Modifier) {
    val boxIcon = getIconByName(gameBox.name)

    Column(
        modifier = modifier
            .border(1.dp, Color.Black),
    ) {
        Image(
            painter = painterResource(id = boxIcon),
            contentDescription = "Box Icon",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.FillBounds
        )
    }
}

/**
 * Parse the icon by name
 */
private fun getIconByName(name: String): Int {
    val normalizedName = name.trim().lowercase()

    return when (normalizedName) {
        BoxName.START.displayName.lowercase() -> R.drawable.icon7
        BoxName.LONELY.displayName.lowercase() -> R.drawable.lonely
        BoxName.TOMATO.displayName.lowercase() -> R.drawable.tomatotown
        BoxName.JAIL.displayName.lowercase() -> R.drawable.icon8
        BoxName.RETAIL.displayName.lowercase() -> R.drawable.retail
        BoxName.LOOT.displayName.lowercase() -> R.drawable.lootlake
        BoxName.LUCK.displayName.lowercase() -> R.drawable.icon9
        BoxName.SALTY.displayName.lowercase() -> R.drawable.salty
        BoxName.PARK.displayName.lowercase() -> R.drawable.park
        BoxName.TAX.displayName.lowercase() -> R.drawable.icon10
        BoxName.GREASY.displayName.lowercase() -> R.drawable.greasy
        BoxName.TILTED.displayName.lowercase() -> R.drawable.tilted

        else -> R.drawable.icon1 // Debug, shouldn't happen
    }
}

/**
 * For  designing
 */
@Preview(showBackground = true, widthDp = 1000, heightDp = 1000)
@Composable
fun DrawBoxesPreview() {
    val topGameBoxes: List<GameBox> = listOf(
        game.model.box.Card(6, BoxName.LUCK.displayName, 12),
        Property(7, BoxName.SALTY.displayName, 140, 10, 100),
        Property(8, BoxName.PARK.displayName, 140, 10, 100),
        Fee(9, BoxName.TAX.displayName, 100),
    )
    val leftGameBoxes: List<GameBox> = listOf(
        Property(4, BoxName.RETAIL.displayName, 60, 2, 50),
        Property(5, BoxName.LOOT.displayName, 60, 4, 50),
    )
    val rightGameBoxes: List<GameBox> = listOf(
        Property(10, BoxName.GREASY.displayName, 60, 2, 50),
        Property(11, BoxName.TILTED.displayName, 60, 4, 50),
    )
    val bottomGameBoxes: List<GameBox> = listOf(
        Jail(3, BoxName.JAIL.displayName),
        Property(2, BoxName.LONELY.displayName, 60, 2, 50),
        Property(1, BoxName.TOMATO.displayName, 60, 4, 50),
        Start(
            0,
            BoxName.START.displayName, 200
        )
    )
    val centerContent: @Composable () -> Unit = {}

    MonopolyTheme {
        BoardArea(
            topGameBoxes = topGameBoxes,
            leftGameBoxes = leftGameBoxes,
            rightGameBoxes = rightGameBoxes,
            bottomGameBoxes = bottomGameBoxes,
            centerContent = centerContent
        )
    }
}