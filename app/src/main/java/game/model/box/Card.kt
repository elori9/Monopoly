package game.model.box

import game.model.Player
import kotlin.random.Random

class Card(
    position: Int,
    name: String,
    val boardSize: Int
) : Box(position, name, BoxType.CARD) {

    override fun action(player: Player) {
        // Pick a random card
        val card = CardType.entries.random()

        // Execute the operation
        when (card) {
            CardType.RECEIVE_MONEY -> {
                player.deposit(100)
            }

            CardType.PAY_MONEY -> {
                player.pay(100)
            }

            CardType.MOVE_TO -> {
                val position = Random.nextInt(0, boardSize)
                player.move(position)
            }
        }
    }
}