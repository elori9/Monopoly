package game.model

import game.model.box.*

class Board {
    var boxes: List<Box> = listOf()
        private set
    var size: Int = 0
        private set

    /**
     * Generates a predefined board based on the number of players.
     *
     * @param numPlayers the number of players (2 to 4)
     */
    fun generateBoard(numPlayers: Int) {
        boxes = when (numPlayers) {
            2 -> generate2PlayerBoard()
            3 -> generate3PlayerBoard()
            else -> generate4PlayerBoard()
        }
        size = boxes.size
    }

    private fun generate2PlayerBoard(): List<Box> {
        val s = 12
        return listOf(
            Start(0, "Autobús de Batalla", 200),
            Property(1, "Soto Solitario", 60, 2, 50),
            Property(2, "Pueblo Tomate", 60, 4, 50),
            Jail(3, "Cárcel"),
            Property(4, "Ciudad Comercio", 100, 6, 50),
            Property(5, "Balsa Botín", 100, 6, 50),
            Card(6, "Suerte", s),
            Property(7, "Señorío de la Sal", 140, 10, 100),
            Property(8, "Parque Placentero", 140, 10, 100),
            Fee(9, "Impuesto", 100),
            Property(10, "Caserío Colesterol", 180, 14, 100),
            Property(11, "Pisos Picados", 200, 16, 100)
        )
    }

    private fun generate3PlayerBoard(): List<Box> {
        val s = 20
        return listOf(
            Start(0, "Autobús de Batalla", 200),
            Property(1, "Soto Solitario", 60, 2, 50),
            Property(2, "Pueblo Tomate", 60, 4, 50),
            Fee(3, "La Tormenta", 100),
            Property(4, "Industrias Inodoras", 100, 6, 50),
            Jail(5, "Cárcel"),
            Property(6, "Polvorín Polvoriento", 100, 6, 50),
            Card(7, "Suerte", s),
            Property(8, "Ribera Repipi", 140, 10, 100),
            Property(9, "Túneles Tortuosos", 140, 10, 100),
            Fee(10, "Fogata", 0),
            Property(11, "Señorío de la Sal", 180, 14, 100),
            Property(12, "Alameda Aullante", 180, 14, 100),
            Card(13, "Suerte", s),
            Property(14, "Latifundio Letal", 220, 18, 150),
            Fee(15, "Trampa de Pinchos", 200),
            Property(16, "Caserío Colesterol", 220, 18, 150),
            Card(17, "Suerte", s),
            Property(18, "Parque Placentero", 260, 22, 200),
            Property(19, "Pisos Picados", 300, 26, 200)
        )
    }

    private fun generate4PlayerBoard(): List<Box> {
        val s = 24
        return listOf(
            Start(0, "Autobús de Batalla", 200),
            Property(1, "Cruce de Chatarra", 60, 2, 50),
            Property(2, "Lomas Lúgubres", 60, 4, 50),
            Fee(3, "La Tormenta", 100),
            Property(4, "Pueblo Tomate", 100, 6, 50),
            Property(5, "Soto Solitario", 100, 6, 50),
            Jail(6, "La Cárcel"),
            Property(7, "Industrias Inodoras", 140, 10, 100),
            Property(8, "Charca Chorreante", 140, 10, 100),
            Card(9, "Suerte", s),
            Property(10, "Polvorín Polvoriento", 180, 14, 100),
            Property(11, "Ribera Repipi", 180, 14, 100),
            Fee(12, "Fogata", 0),
            Property(13, "Túneles Tortuosos", 220, 18, 150),
            Property(14, "Aterrizaje Afortunado", 220, 18, 150),
            Card(15, "Suerte", s),
            Property(16, "Señorío de la Sal", 260, 22, 200),
            Property(17, "Alameda Aullante", 260, 22, 200),
            Fee(18, "Trampa de Pinchos", 200),
            Property(19, "Latifundio Letal", 300, 26, 200),
            Property(20, "Caserío Colesterol", 300, 26, 200),
            Card(21, "Suerte", s),
            Property(22, "Parque Placentero", 350, 35, 250),
            Property(23, "Pisos Picados", 400, 50, 300)
        )
    }

    /**
     * Returns the box at the given position.
     * Uses modulo to allow circular movement.
     *
     * @param positionIndex the index of the box
     * @return the box at that position
     */
    fun getBox(positionIndex: Int): Box {
        return boxes[positionIndex % size]
    }
}
