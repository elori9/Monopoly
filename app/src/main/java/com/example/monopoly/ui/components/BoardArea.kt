package com.example.monopoly.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.monopoly.ui.theme.MonopolyTheme
import game.model.box.GameBox
import game.model.box.BoxType
import game.model.box.Start
import com.example.monopoly.R

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
            topGameBoxes.forEach { box -> DrawBox(box) }
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
                leftGameBoxes.forEach { box -> DrawBox(box) }
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
                rightGameBoxes.forEach { box -> DrawBox(box) }
            }
        }

        // Bottom
        Row(modifier = Modifier.fillMaxWidth()) {
            // Draw all boxes
            bottomGameBoxes.forEach { box -> DrawBox(box) }
        }
    }

}


@Composable
fun DrawBox(gameBox: GameBox) {
    val boxIcon = when (gameBox.type) {
        BoxType.PROPERTY -> R.drawable.icon1
        BoxType.START -> R.drawable.icon7
        BoxType.JAIL -> R.drawable.icon8
        BoxType.CARD -> R.drawable.icon9
        BoxType.FEE -> R.drawable.icon10
    }

    Column(
        modifier = Modifier
            .border(1.dp, Color.Black),
    ) {
        Image(
            painter = painterResource(id = boxIcon),
            contentDescription = "Box Icon",
            modifier = Modifier.fillMaxSize(),
        )
    }
}


@Preview(showBackground = true, widthDp = 100, heightDp = 100)
@Composable
fun DrawBoxPreview() {
    MonopolyTheme {
        DrawBox(Start(1, "Start", 200))
    }
}