package game.model

import kotlin.random.Random

open class Dice {
    // Use private set for make it readable and non-writeable
    var numbers: Int = 0
        private set

    /**
     * Rolls 2 dices
     *
     * @return The result of both combined (2-12)
     */
    open fun roll(): Int {
        val dice = Random.nextInt(1, 7)

        numbers = dice

        return numbers
    }
}
