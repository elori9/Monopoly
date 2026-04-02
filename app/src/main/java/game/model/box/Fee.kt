package game.model.box

import game.model.Player

class Fee
    (
    position: Int,
    name: String,
    val money: Int
) : GameBox(position, name, BoxType.FEE) {

    override fun action(player: Player): String {
        player.pay(money)
        return ""
    }
}