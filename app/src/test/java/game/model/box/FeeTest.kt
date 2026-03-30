package game.model.box

import game.model.Player
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class FeeTest {
    private lateinit var player: Player
    private lateinit var feeBox: Fee

    @BeforeEach
    fun setUp() {
        player = Player("PLAYER", 0, 1000)

        feeBox = Fee(4, "FEE", 200)
    }

    @Test
    @DisplayName("Action deduct the amount from the player")
    fun testFeeActionCorrect() {
        feeBox.action(player)

        assertEquals(800, player.money)
    }

    @Test
    @DisplayName("Action should make player broke if they can't pay the fee")
    fun testFeeActionBroke() {
        val poorPlayer = Player("PLAYER", 1, 50)

        feeBox.action(poorPlayer)

        assertTrue(poorPlayer.broke)
    }
}