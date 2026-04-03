package game.model

import game.model.box.Property

class Player(
    val name: String,
    val id: Int,
    var money: Int,
) {
    var properties: MutableList<Property> = mutableListOf()
        private set
    var position: Int = 0
        private set
    var jail: Boolean = false
    var jailTurns: Int = 0
    var broke: Boolean = false
        private set

    /**
     * Move the players to a position
     *
     * @param boxIndex the position
     */
    fun move(boxIndex: Int) {
        position = boxIndex
    }

    /**
     * Tries to pay money
     *
     * @param money the amount of money
     */
    fun pay(amount: Int) {
        if (money < amount) {
            // Can't pay it -> broke
            broke = true
        } else {
            // Pay
            money -= amount
        }
    }

    /**
     * Deposit money
     *
     * @param money the quantity of money to gain
     */
    fun deposit(amount: Int) {
        money += amount
    }

    /**
     * Tries to buy a property
     *
     * @param property the property
     */
    fun buyProperty(property: Property) {
        if (money < property.price) {
            // Can't afford it -> will send message
        } else {
            // Pay the property
            pay(property.price)
            // Save the property
            properties.add(property)
            // Assign the property
            property.owner = this
        }
    }

    /**
     * Buy a house
     * @param property the property where to build
     */
    fun buyHouse(property: Property) {
        if (property.owner != this) {
            // Cant buy if im not owner
            return
        }

        if (property.numHouses >= 5) {
            // Just five houses maximum
            return
        }

        if (money < property.housePrice) {
            // buy if we have money
            return
        }

        // Buy it
        pay(property.housePrice)
        property.numHouses += 1
    }

    /**
     * Calculate the networth
     *
     * @return the amount of money
     */
    fun calculateNetworth(): Int {
        var totalNetworth: Int = money

        for (property in properties){
            totalNetworth += property.housePrice * property.numHouses
            totalNetworth += property.price
        }

        return totalNetworth
    }

    // --- TESTING ---
    /**
     * For testing
     */
    fun setBroke(bool: Boolean) {
        broke = bool
    }
    fun setPosition(pos: Int) {
        position = pos
    }
}