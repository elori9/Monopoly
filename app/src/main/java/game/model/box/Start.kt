package game.model.box

import game.model.Player

class Start(
    position: Int,
    name: String,
    val money: Int
) : Box(position, name, BoxType.START) {

    override fun action(player: Player): String {
        player.deposit(money)
        return ""
    }
}