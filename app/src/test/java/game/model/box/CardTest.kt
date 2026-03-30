package game.model.box
import game.model.Player
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class CardTest {
    private lateinit var player: Player
    private lateinit var cardBox: Card
    private val boardSize = 24

    @BeforeEach
    fun setUp() {
        player = Player("PLAYER", 0, 1000)
        cardBox = Card(5, "CARD", boardSize)
    }

    @Test
    @DisplayName("Card action should execute one of the 3 valid random outcomes")
    fun testRandomCardAction() {
        cardBox.action(player)

        // Check all three randoms scenarios: receive money, pay money or moved somewhere on the board
        val receivedMoney = (player.money == 1100 && player.position == 0)
        val paidMoney = (player.money == 900 && player.position == 0)
        val moved = (player.money == 1000 && player.position >= 0 && player.position < boardSize)

        // One of three conditions must be true
        assertTrue(receivedMoney || paidMoney || moved)
    }
}