package game.model.box

import game.model.Player

class Jail
    (
    position: Int,
    name: String,
) : Box(position, name, BoxType.JAIL) {

    override fun action(player: Player) {
        player.jail = true
        player.jailTurns = 3
    }
}