package com.example.monopoly.ui.components

import android.icu.text.ListFormatter
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
import game.model.box.BoxType
import game.model.box.Start
import com.example.monopoly.R
import game.model.Board
import game.model.box.BoxName
import game.model.box.Jail

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
        Row(modifier = Modifier.fillMaxWidth()) {
            // Draw all boxes
            topGameBoxes.forEach { box -> DrawBox(box, modifier = Modifier.weight(1f)) }
        }

        // Middle
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {

            // Left boxes
            Column {
                // Draw all boxes
                leftGameBoxes.forEach { box -> DrawBox(box, modifier = Modifier.weight(1f)) }
            }

            // Middle space for dice
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
            ) {
                centerContent()
            }

            // Right boxes
            Column {
                // Draw all boxes
                rightGameBoxes.forEach { box -> DrawBox(box, modifier = Modifier.weight(1f)) }
            }
        }

        // Bottom
        Row(modifier = Modifier.fillMaxWidth()) {
            // Draw all boxes
            bottomGameBoxes.forEach { box -> DrawBox(box, modifier = Modifier.weight(1f)) }
        }
    }

}


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

private fun getIconByName(name: String): Int {
    val normalizedName = name.trim().lowercase()

    return when (normalizedName) {
        BoxName.START.displayName.lowercase()    -> R.drawable.icon7
        BoxName.LONELY.displayName.lowercase() -> R.drawable.lonely
        BoxName.TOMATO.displayName.lowercase() -> R.drawable.tomatotown
        BoxName.JAIL.displayName.lowercase()   -> R.drawable.icon8
        BoxName.RETAIL.displayName.lowercase() -> R.drawable.retail
        BoxName.LOOT.displayName.lowercase()   -> R.drawable.lootlake
        BoxName.LUCK.displayName.lowercase()   -> R.drawable.icon9
        BoxName.SALTY.displayName.lowercase()  -> R.drawable.salty
        BoxName.PARK.displayName.lowercase()   -> R.drawable.park
        BoxName.TAX.displayName.lowercase()    -> R.drawable.icon10
        BoxName.GREASY.displayName.lowercase() -> R.drawable.greasy
        BoxName.TILTED.displayName.lowercase() -> R.drawable.tilted

        else -> R.drawable.icon1 // Icono de debug
    }
}

/**
 * For  designing
 */
@Preview(showBackground = true, widthDp = 1000, heightDp = 1000)
@Composable
fun DrawBoxesPreview() {
    val topGameBoxes: List<GameBox> = listOf()
    val leftGameBoxes: List<GameBox> = listOf()
    val rightGameBoxes: List<GameBox> = listOf()
    val bottomGameBoxes: List<GameBox> = listOf(
        Jail(3, BoxName.JAIL.displayName),
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