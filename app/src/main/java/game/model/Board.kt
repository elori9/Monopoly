package game.model

import game.model.box.*

class Board {
    var gameBoxes: List<GameBox> = listOf()
        private set
    var size: Int = 0
        private set

    /**
     * Generates a predefined board based on the number of players.
     *
     * @param numPlayers the number of players (2 to 4)
     */
    fun generateBoard(numPlayers: Int) {
        gameBoxes = when (numPlayers) {
            2 -> generate2PlayerBoard()
            3 -> generate3PlayerBoard()
            else -> generate4PlayerBoard()
        }
        size = gameBoxes.size
    }

    private fun generate2PlayerBoard(): List<GameBox> {
        val s = 12
        return listOf(
            Start(0, BoxName.START.displayName, 200),
            Property(1, BoxName.LONELY.displayName, 60, 2, 50),
            Property(2, BoxName.TOMATO.displayName, 60, 4, 50),
            Jail(3, BoxName.JAIL.displayName),
            Property(4, BoxName.RETAIL.displayName, 100, 6, 50),
            Property(5, BoxName.LOOT.displayName, 100, 6, 50),
            Card(6, BoxName.LUCK.displayName, s),
            Property(7, BoxName.SALTY.displayName, 140, 10, 100),
            Property(8, BoxName.PARK.displayName, 140, 10, 100),
            Fee(9, BoxName.TAX.displayName, 100),
            Property(10, BoxName.GREASY.displayName, 180, 14, 100),
            Property(11, BoxName.TILTED.displayName, 200, 16, 100),
        )
    }

    private fun generate3PlayerBoard(): List<GameBox> {
        val s = 20
        return listOf(
            Start(0, BoxName.START.displayName, 200),
            Property(1, BoxName.LONELY.displayName, 60, 2, 50),
            Property(2, BoxName.TOMATO.displayName, 60, 4, 50),
            Fee(3, BoxName.STORM.displayName, 100),
            Property(4, BoxName.FLUSH.displayName, 100, 6, 50),
            Jail(5, BoxName.JAIL.displayName),
            Property(6, BoxName.DUSTY.displayName, 100, 6, 50),
            Card(7, BoxName.LUCK.displayName, s),
            Property(8, BoxName.SNOBBY.displayName, 140, 10, 100),
            Property(9, BoxName.SHIFTY.displayName, 140, 10, 100),
            Fee(10, BoxName.CAMPFIRE.displayName, 0),
            Property(11, BoxName.SALTY.displayName, 180, 14, 100),
            Property(12, BoxName.WAILING.displayName, 180, 14, 100),
            Card(13, BoxName.LUCK.displayName, s),
            Property(14, BoxName.FATAL.displayName, 220, 18, 150),
            Fee(15, BoxName.SPIKE_TRAP.displayName, 200),
            Property(16, BoxName.GREASY.displayName, 220, 18, 150),
            Card(17, BoxName.LUCK.displayName, s),
            Property(18, BoxName.PARK.displayName, 260, 22, 200),
            Property(19, BoxName.TILTED.displayName, 300, 26, 200)
        )
    }

    private fun generate4PlayerBoard(): List<GameBox> {
        val s = 24
        return listOf(
            Start(0, BoxName.START.displayName, 200),
            Property(1, BoxName.JUNK.displayName, 60, 2, 50),
            Property(2, BoxName.HAUNTED.displayName, 60, 4, 50),
            Fee(3, BoxName.STORM.displayName, 100),
            Property(4, BoxName.TOMATO.displayName, 100, 6, 50),
            Property(5, BoxName.LONELY.displayName, 100, 6, 50),
            Jail(6, BoxName.JAIL.displayName),
            Property(7, BoxName.FLUSH.displayName, 140, 10, 100),
            Property(8, BoxName.MOISTY.displayName, 140, 10, 100),
            Card(9, BoxName.LUCK.displayName, s),
            Property(10, BoxName.DUSTY.displayName, 180, 14, 100),
            Property(11, BoxName.SNOBBY.displayName, 180, 14, 100),
            Fee(12, BoxName.CAMPFIRE.displayName, 0),
            Property(13, BoxName.SHIFTY.displayName, 220, 18, 150),
            Property(14, BoxName.LUCKY_LANDING.displayName, 220, 18, 150),
            Card(15, BoxName.LUCK.displayName, s),
            Property(16, BoxName.SALTY.displayName, 260, 22, 200),
            Property(17, BoxName.WAILING.displayName, 260, 22, 200),
            Fee(18, BoxName.SPIKE_TRAP.displayName, 200),
            Property(19, BoxName.FATAL.displayName, 300, 26, 200),
            Property(20, BoxName.GREASY.displayName, 300, 26, 200),
            Card(21, BoxName.LUCK.displayName, s),
            Property(22, BoxName.PARK.displayName, 350, 35, 250),
            Property(23, BoxName.TILTED.displayName, 400, 50, 300)
        )
    }

    /**
     * Returns the box at the given position.
     * Uses modulo to allow circular movement.
     *
     * @param positionIndex the index of the box
     * @return the box at that position
     */
    fun getBox(positionIndex: Int): GameBox {
        return gameBoxes[positionIndex % size]
    }
}