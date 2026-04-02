package com.example.monopoly.ui.components.animations

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.example.monopoly.R
import game.model.Dice


@Composable
fun RollDice(modifier: Modifier = Modifier, result: Int, onRollClick: () -> Unit) {
    val imageResource = when (result) {
        1 -> R.drawable.dice_1
        2 -> R.drawable.dice_2
        3 -> R.drawable.dice_3
        4 -> R.drawable.dice_4
        5 -> R.drawable.dice_5
        else -> R.drawable.dice_6
    }
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Image(
            painter = painterResource(imageResource),
            contentDescription = result.toString()
        )

        Button(
            onClick = onRollClick,
        ) {
            Text(text = stringResource(R.string.roll), fontSize = 24.sp)
        }
    }
}

/**
 * Test preview
 */
@Preview
@Composable
fun DiceRollerApp() {
    val myDice = remember { Dice() }

    var diceValue by remember { mutableIntStateOf(1) }

    RollDice(
        modifier = Modifier,
        result = diceValue,
        onRollClick = {
            diceValue = myDice.roll()
        }
    )
}