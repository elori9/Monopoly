package game.model.box

import game.model.Player

abstract class Box(
    val position: Int,
    val name: String,
    val type: BoxType
) {
    /**
     * Make an action on the box
     */
    abstract fun action(player: Player): String
}