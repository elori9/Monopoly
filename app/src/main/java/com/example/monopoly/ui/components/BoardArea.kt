package com.example.monopoly.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.monopoly.ui.theme.MonopolyTheme
import game.model.box.GameBox
import game.model.box.Start
import com.example.monopoly.R
import com.example.monopoly.ui.components.animations.RollDice
import game.model.box.BoxName
import game.model.box.Fee
import game.model.box.Jail
import game.model.box.Property
import game.model.Player

/**
 * Draw the board
 */
@Composable
fun BoardArea(
    topGameBoxes: List<GameBox>,
    bottomGameBoxes: List<GameBox>,
    leftGameBoxes: List<GameBox>,
    rightGameBoxes: List<GameBox>,
    allPlayers: List<Player>,
    gameMessage: String,
    centerContent: @Composable () -> Unit // Save space for dices in the middle
) {
    Column(modifier = Modifier.fillMaxWidth()) {

        // Top
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.25f)
        ) {
            // Draw all boxes
            topGameBoxes.forEach { box ->
                DrawBox(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                    gameBox = box,
                    playersHere = allPlayers.filter { player -> player.position == box.position },
                    housesCount = if (box is Property) box.numHouses else 0
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
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight(),
                        gameBox = box,
                        playersHere = allPlayers.filter { player -> player.position == box.position },
                        housesCount = if (box is Property) box.numHouses else 0
                    )
                }
            }

            // Middle space for dice
            Box(
                modifier = Modifier
                    .weight(0.5f)
                    .fillMaxHeight()
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp)
                ) {
                    // Messages
                    Text(
                        text = gameMessage,
                        fontSize = 18.sp,
                        fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )

                    // Dice
                    centerContent()
                }
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
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight(),
                        gameBox = box,
                        playersHere = allPlayers.filter { player -> player.position == box.position },
                        housesCount = if (box is Property) box.numHouses else 0
                    )
                }
            }
        }

        // Bottom
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.25f)
        ) {
            // Draw all boxes
            bottomGameBoxes.forEach { box ->
                DrawBox(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                    gameBox = box,
                    playersHere = allPlayers.filter { player -> player.position == box.position },
                    housesCount = if (box is Property) box.numHouses else 0
                )
            }
        }
    }
}


/**
 * Draw the box
 */
@Composable
fun DrawBox(
    modifier: Modifier = Modifier,
    gameBox: GameBox,
    playersHere: List<Player> = emptyList(),
    housesCount: Int = 0,
) {
    val boxIcon = getIconByName(gameBox.name)

    // Use of box to use a cape container
    Box(
        modifier = modifier.border(1.dp, Color.Black)
    ) {
        //Background
        Image(
            painter = painterResource(id = boxIcon),
            contentDescription = "Box Icon",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.FillBounds
        )

        // Houses (on top)
        if (housesCount > 0) {
            Row(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                repeat(housesCount) {
                    Image(
                        painter = painterResource(id = R.drawable.icon6),
                        contentDescription = "house",
                        modifier = Modifier.size(15.dp)
                    )
                }
            }
        }

        // Players icons (on bottom)
        if (playersHere.isNotEmpty()) {
            Row(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 6.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                playersHere.forEach { player ->
                    val tokenImage = getTokenByPlayerId(player.id)

                    Image(
                        painter = painterResource(id = tokenImage),
                        contentDescription = "Piece of ${player.name}",
                        modifier = Modifier.size(50.dp)
                    )
                }
            }
        }
    }
}

/**
 * Parse the player to an icon
 */
fun getTokenByPlayerId(playerId: Int): Int {
    return when (playerId) {
        0 -> R.drawable.icon1
        1 -> R.drawable.icon2
        2 -> R.drawable.icon3
        3 -> R.drawable.icon4
        else -> R.drawable.icon1 // Shouldn't happen
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
        Property(11, BoxName.TILTED.displayName, 60, 4, 50).apply { numHouses = 4 },
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

    val allPlayers: List<Player> = listOf(
        // 2 players on same box
        Player(id = 0, name = "P1", money = 2000).apply { setPosition(0) },
        Player(id = 1, name = "P2", money = 2000).apply { setPosition(0) },

        // P3 on jail
        Player(id = 2, name = "P3", money = 2000).apply { setPosition(3) },

        // P4 on a property with 4 houses
        Player(id = 3, name = "P4", money = 2000).apply { setPosition(11) }
    )

    MonopolyTheme {
        BoardArea(
            topGameBoxes = topGameBoxes,
            leftGameBoxes = leftGameBoxes,
            rightGameBoxes = rightGameBoxes,
            bottomGameBoxes = bottomGameBoxes,
            allPlayers = allPlayers,
            gameMessage = "MSG",
            centerContent = centerContent
        )
    }
}