package game.model.box

import game.model.Player

class Jail
    (
    position: Int,
    name: String,
    val jailTurns: Int = 3
) : GameBox(position, name, BoxType.JAIL) {

    override fun action(player: Player): String {
        player.jail = true
        player.jailTurns = jailTurns
        return ""
    }
}