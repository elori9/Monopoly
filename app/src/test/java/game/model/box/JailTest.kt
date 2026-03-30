package game.model.box

import game.model.Player
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName

class JailTest {
    private lateinit var player: Player
    private lateinit var jailBox: Jail

    @BeforeEach
    fun setUp() {
        player = Player("PLAYER", 0, 1000)
        jailBox = Jail(10, "JAIL")
    }

    @Test
    @DisplayName("Action send player to jail for 3 turns")
    fun testJailAction() {
        jailBox.action(player)

        assertTrue(player.jail)
        assertEquals(3, player.jailTurns)
    }
}