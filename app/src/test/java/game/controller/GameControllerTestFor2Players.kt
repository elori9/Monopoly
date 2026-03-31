package game.controller

import game.model.Board
import game.model.Dice
import game.model.Player
import game.model.TurnAction
import game.model.box.Property
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`

class GameControllerTestFor2Players {

    private lateinit var controller: GameController
    private lateinit var mockView: MockGameView
    private lateinit var board: Board
    private lateinit var dice: Dice
    private lateinit var players: List<Player>

    @BeforeEach
    fun setUp() {
        mockView = MockGameView()
        board = Board()

        // Use mockito for mock the dice
        dice = mock(Dice::class.java)

        // Two players game
        players = listOf(
            Player(name = "PLAYER 1", id = 0, money = 2000),
            Player(name = "PLAYER 2", id = 1, money = 2000)
        )

        board.generateBoard(2)

        controller = GameController(mockView, board, players, 0, dice)
    }

    @Test
    @DisplayName("Start game and player 1 roll the dice and buy a house, player 2 roll and falls in to it and pays")
    fun startGameAndPayRent() {
        // Configure the actions
        mockView.defaultTurnAction = TurnAction.ROLL_DICE
        mockView.buyProperty = true
        mockView.turnsToPlay = 2

        // The rolls of the dice will be 2
        `when`(dice.roll()).thenReturn(2, 2)

        // Start game
        controller.startGame()

        val p1 = players[0]
        val p2 = players[1]
        val prop = board.getBox(2) as Property

        // Both players must land on the property
        assertEquals(2, p1.position)
        assertEquals(2, p2.position)

        // Player 1 must have bought the property
        assertEquals(p1, prop.owner)

        // Player 2 and player 1 updated money
        assertEquals(1944, p1.money)
        assertEquals(1996, p2.money)
    }

    @Test
    @DisplayName("Player 1 falls in a fee with no money")
    fun testBankruptOnFee() {
        // Configure the actions
        mockView.defaultTurnAction = TurnAction.ROLL_DICE
        mockView.turnsToPlay = 1

        // Sub the money for p1
        val p1 = players[0]
        p1.money = 10

        // The box 9 is fee
        `when`(dice.roll()).thenReturn(9)

        // Start game
        controller.startGame()

        // Check the GUI for bankruptcies
        assertTrue(mockView.bankruptcies.contains(p1.name))

        // Turn is for player 2
        assertEquals(1, controller.turn)
    }

    @Test
    @DisplayName("Player 1 falls on jail")
    fun testOnJail() {
        // Configure the actions
        mockView.defaultTurnAction = TurnAction.ROLL_DICE
        mockView.turnsToPlay = 2

        // First roll go jail
        // Second roll, p2 play
        `when`(dice.roll()).thenReturn(3, 2)

        // Start game
        controller.startGame()

        val p1 = players[0]
        val p2 = players[1]

        // Player 1 felt on jail and don't move later
        assertEquals(3, p1.position)

        // Player 2 can play
        assertEquals(2, p2.position)

        // P2 turn, as p1 is on jail
        assertEquals(1, controller.turn)
    }

    @Test
    @DisplayName("Player 1 buy property, p2 move, p1 build a house and P2 move -> fall into it and pay")
    fun tesBuildHouses() {
        // Configurations
        val p1 = players[0]
        val p2 = players[1]
        val property = board.getBox(2) as Property
        mockView.turnsToPlay = 1
        mockView.turnsPlayed = 0


        // p1 buy the 2nd house
        mockView.defaultTurnAction = TurnAction.ROLL_DICE
        `when`(dice.roll()).thenReturn(2)
        mockView.buyProperty = true
        controller.startGame()

        assertEquals(1940, p1.money)


        // p2 turn -> falls on a house with no owner and don't buy it (no pay too)
        // reset the turn counter
        mockView.turnsToPlay = 1
        mockView.turnsPlayed = 0
        mockView.buyProperty = false
        `when`(dice.roll()).thenReturn(1)
        controller.nextTurn()

        assertEquals(2000, p2.money)
        assertEquals(0, p2.properties.size)


        // p1 build
        // reset the turn counter
        mockView.defaultTurnAction = TurnAction.BUILD_HOUSE
        mockView.turnsToPlay = 1
        mockView.turnsPlayed = 0
        mockView.propertyToBuild = property
        controller.nextTurn()

        assertEquals(1890, p1.money)


        // p1 roll the dices to end his turn
        // reset the turn counter
        mockView.defaultTurnAction = TurnAction.ROLL_DICE
        mockView.turnsToPlay = 1
        mockView.turnsPlayed = 0
        `when`(dice.roll()).thenReturn(1)
        controller.nextTurn()


        // P2 falls and pay
        // reset the turn counter
        mockView.defaultTurnAction = TurnAction.ROLL_DICE
        mockView.turnsToPlay = 1
        mockView.turnsPlayed = 0
        `when`(dice.roll()).thenReturn(1)
        controller.nextTurn()

        // Price =  rent + (numHouses * housePrice / 2)
        assertEquals(1971, p2.money)
        assertEquals(1919, p1.money)
    }

    @Test
    @DisplayName("Game ends when time limit is reached, and P1 wins for networth")
    fun testTimeLimit() {
        // Time limit 1 min
        val timeLimit = 1
        val timeController = GameController(mockView, board, players, timeLimit, dice)

        // Let play 1 round
        mockView.turnsToPlay = 1
        mockView.turnsPlayed = 0
        `when`(dice.roll()).thenReturn(1)

        // Start the controller
        timeController.startGame()

        // Calculate the past
        val ago61Seconds = System.currentTimeMillis() - (61 * 1000)
        timeController.setStartTime(ago61Seconds)

        // Player 1 has more money to win
        players[0].money = 5000
        players[1].money = 1000

        // Reset mock for check time
        mockView.turnsToPlay = 1
        mockView.turnsPlayed = 0

        //  nextTurn for checkTime()
        timeController.nextTurn()

        assertTrue(mockView.isGameOver)
        assertEquals("PLAYER 1", mockView.winnerName)
    }

    @Test
    @DisplayName("Player 1 falls on Card")
    fun testCardTeleport() {
        val p1 = players[0]

        // p1 fall in cards
        `when`(dice.roll()).thenReturn(6)
        mockView.defaultTurnAction = TurnAction.ROLL_DICE
        mockView.turnsToPlay = 1

        controller.startGame()

        assertTrue(p1.position != 6 || p1.money != 2000)
    }

    @Test
    @DisplayName("Game ends when only one player is not broke")
    fun testLastSurvivor() {
        val p2 = players[1]
        p2.setBroke(true)

        mockView.turnsToPlay = 1
        controller.nextTurn()

        assertTrue(mockView.isGameOver)
    }

    @Test
    @DisplayName("Player gets money when passing through Start box")
    fun testPassStart() {
        val p1 = players[0]
        p1.move(11)

        // move and don't buy
        `when`(dice.roll()).thenReturn(2)
        mockView.buyProperty = false
        mockView.turnsToPlay = 1
        mockView.turnsPlayed = 0

        controller.nextTurn()

        assertEquals(2200, p1.money)
    }

}