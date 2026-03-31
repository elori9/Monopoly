package game.model.box

import game.model.Player

class Property(
    position: Int,
    name: String,
    val price: Int,
    val rent: Int,
    val housePrice: Int
) : Box(position, name, BoxType.PROPERTY) {

    var owner: Player? = null
    var numHouses: Int = 0

    override fun action(player: Player): String {
        if (owner != null && owner != player) {
            // Someone felt on a property:
            // Not my house, not buy
            val currentRent = rent + (numHouses * housePrice / 2)

            // Make the player pay
            player.pay(currentRent)
            // Give the money for the owner
            owner?.deposit(currentRent)
        }
        return ""
    }
}