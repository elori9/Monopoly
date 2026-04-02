package com.example.monopoly.ui.components.animations

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.example.monopoly.R
import game.model.Dice

/**
 * Roll dice animation
 */
@Composable
fun RollDice(modifier: Modifier = Modifier, result: Int, onRollClick: () -> Unit) {
    // Rotation state
    var rotation by remember { mutableFloatStateOf(0f) }

    // Animate
    val rotationDice by animateFloatAsState(
        targetValue = rotation,
        animationSpec = tween (durationMillis = 250, easing = LinearEasing),
        label = "rotation"
    )

    // Recreate when rotation change state
    LaunchedEffect (result) {
        if (result != 1 || rotation != 0f) {
            // For just rolling on click
            rotation += 360f
        }
    }

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
            contentDescription = result.toString(),
            // Make rotation
            modifier = Modifier.graphicsLayer{
                rotationZ = rotationDice
            }
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
