package game.model.box

import game.model.Player
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName

class StartTest {
    private lateinit var player: Player
    private lateinit var startBox: Start

    @BeforeEach
    fun setUp() {
        player = Player("PLAYER", 0, 1000)
        startBox = Start(0, "START", 200)
    }

    @Test
    @DisplayName("Action should deposit money to player")
    fun testStartAction() {
        startBox.action(player)
        assertEquals(1200, player.money)
    }
}