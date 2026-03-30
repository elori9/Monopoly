package game.model.box

import game.model.Player
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class PropertyTest {
    private lateinit var owner: Player
    private lateinit var payer: Player

    private lateinit var property: Property

    @BeforeEach
    fun setUp() {
        owner = Player("OWNER", 0, 1000)
        payer = Player("PAYER", 1, 1000)
        property = Property(10, "PROPERTY", 100, 10, 50)

        property.owner = owner
    }

    @Test
    @DisplayName("Make the player pay if is not his property, with no houses")
    fun testPropertyActionNonHouseCorrect() {
        property.action(payer)

        // Sub the money to the payer
        assertEquals(990, payer.money)

        // Give the money to the owner
        assertEquals(1010, owner.money)
    }

    @Test
    @DisplayName("Make the player pay if is not his property, with houses")
    fun testPropertyActionHouseCorrect() {
        // Add a house
        property.numHouses = 1

        property.action(payer)

        // Sub the money to the payer
        assertEquals(965, payer.money)

        // Give the money to the owner
        assertEquals(1035, owner.money)
    }

    @Test
    @DisplayName("Don't make the player pay bc is not his property")
    fun testPropertyActionIncorrect() {
        property.action(owner)

        // Don't sub the money to the owner and don't give the money to the owner
        assertEquals(1000, owner.money)
    }

    @Test
    @DisplayName("Don't pay rent if property has no owner")
    fun testPropertyActionNoOwner() {
        // Make no owner
        property.owner = null

        // Don't pay
        property.action(payer)
        assertEquals(1000, payer.money)
    }


}