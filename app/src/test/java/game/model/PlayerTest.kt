package game.model

import game.model.box.Property
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName

class PlayerTest {
    lateinit var player1: Player
    lateinit var property: Property

    @BeforeEach
    fun setUp() {
        player1 = Player("Player1", 0, 1000)
    }


    @Test
    @DisplayName("Move")
    fun testMove() {
        assertEquals(0, player1.position)
        player1.move(3)
        assertEquals(3, player1.position)
    }


    @Test
    @DisplayName("Decrease money")
    fun testMoneyPay() {
        player1.pay(100)

        assertEquals(900, player1.money)
    }

    @Test
    @DisplayName("Pay and don't have money")
    fun testCantMoneyPay() {
        player1.pay(1100)

        assertTrue(player1.broke)
    }


    @Test
    @DisplayName("Deposit on player increase money")
    fun testMoneyDeposit() {
        player1.deposit(100)

        assertEquals(1100, player1.money)
    }

    fun initPropertyAndBuy() {
        property = Property(0, "HOUSE", 100, 10, 50)
        player1.buyProperty(property)
    }

    @Test
    @DisplayName("Buy property correctly")
    fun testBuyPropertyCorrect() {
        initPropertyAndBuy()

        assertEquals(property, player1.properties[0])
        assertEquals(900, player1.money)
        assertEquals(player1, property.owner)
    }

    @Test
    @DisplayName("Cant buy property due to price")
    fun testCantBuyPropertyCorrect() {
        val expensiveProperty = Property(1, "MANSION", 5000, 500, 200)

        player1.buyProperty(expensiveProperty)

        assertTrue(player1.properties.isEmpty())
        assertEquals(1000, player1.money)
    }


    @Test
    @DisplayName("Buy house correct")
    fun testCanBuyHouse() {
        initPropertyAndBuy()
        val myProperty = player1.properties[0]

        val moneyBeforeHouse = player1.money

        player1.buyHouse(myProperty)

        assertEquals(1, myProperty.numHouses)
        assertEquals(moneyBeforeHouse - myProperty.housePrice, player1.money)
    }

    @Test
    @DisplayName("Cant buy house if not owner")
    fun testCantBuyHouseNotOwner() {
        val foreignProperty = Property(2, "STREET", 100, 10, 50)

        player1.buyHouse(foreignProperty)

        assertEquals(0, foreignProperty.numHouses)
        assertEquals(1000, player1.money)
    }

    @Test
    @DisplayName("Cant buy more than 5 houses (Limit check)")
    fun testHouseLimit() {
        initPropertyAndBuy()
        val myProperty = player1.properties[0]
        val moneyBeforeHouses = player1.money

        for (i in 1..6) {
            player1.buyHouse(myProperty)
        }

        assertEquals(5, myProperty.numHouses)

        assertEquals(moneyBeforeHouses - (5 * myProperty.housePrice), player1.money)
    }


    @Test
    @DisplayName("Property networth with house and property and money")
    fun testNetworth() {
        initPropertyAndBuy()
        assertEquals(1000, player1.calculateNetworth())

        player1.buyHouse(property)
        assertEquals(1000, player1.calculateNetworth())

        player1.pay(100)
        assertEquals(900, player1.calculateNetworth())

        player1.deposit(100)
        assertEquals(1000, player1.calculateNetworth())
    }

}