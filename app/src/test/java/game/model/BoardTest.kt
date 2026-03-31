package game.model

import game.model.box.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class BoardTest {
    private lateinit var board: Board

    @BeforeEach
    fun setUp() {
        board = Board()
    }

    @Test
    @DisplayName("Generate board for 2 players")
    fun testGenerateBoard2Players() {
        board.generateBoard(2)
        assertEquals(12, board.size)
        assertEquals(12, board.boxes.size)
        
        assertTrue(board.getBox(0) is Start)
        assertTrue(board.getBox(3) is Jail)
        assertTrue(board.getBox(6) is Card)
        assertTrue(board.getBox(9) is Fee)
    }

    @Test
    @DisplayName("Generate board for 3 players")
    fun testGenerateBoard3Players() {
        board.generateBoard(3)
        assertEquals(20, board.size)
        assertEquals(20, board.boxes.size)
        
        assertTrue(board.getBox(0) is Start)
        assertTrue(board.getBox(5) is Jail)
        assertTrue(board.getBox(10) is Fee)
        assertTrue(board.getBox(15) is Fee)
    }

    @Test
    @DisplayName("Generate board for 4 players")
    fun testGenerateBoard4Players() {
        board.generateBoard(4)
        assertEquals(24, board.size)
        assertEquals(24, board.boxes.size)
        
        assertTrue(board.getBox(0) is Start)
        assertTrue(board.getBox(6) is Jail)
        assertTrue(board.getBox(12) is Fee)
        assertTrue(board.getBox(18) is Fee)
    }

    @Test
    @DisplayName("Get box with modulo (circular board)")
    fun testGetBoxModulo() {
        board.generateBoard(2) // size 12
        
        val box0 = board.getBox(0)
        val box12 = board.getBox(12)
        val box24 = board.getBox(24)
        
        assertSame(box0, box12)
        assertSame(box0, box24)
        assertTrue(box12 is Start)
    }

    @Test
    @DisplayName("Verify properties in 4 player board")
    fun testProperties4Players() {
        board.generateBoard(4)
        
        val firstProp = board.getBox(1)
        assertTrue(firstProp is Property)
        assertEquals("Cruce de Chatarra", firstProp.name)
        assertEquals(60, (firstProp as Property).price)
        
        val lastProp = board.getBox(23)
        assertTrue(lastProp is Property)
        assertEquals("Pisos Picados", lastProp.name)
        assertEquals(400, (lastProp as Property).price)
    }
}
