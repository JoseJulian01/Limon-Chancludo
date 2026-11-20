package mx.udg.limonchancludo.game.model

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class CharacterTest {

    @Test
    fun `there are exactly six characters with unique ids`() {
        assertEquals(6, Characters.ALL.size)
        assertEquals(6, Characters.ALL.map { it.id }.toSet().size)
    }

    @Test
    fun `unlock thresholds increase with the PRD values`() {
        val expected = listOf(0, 5_000, 10_000, 20_000, 35_000, 50_000)
        assertEquals(expected, Characters.ALL.map { it.unlockScore })
    }

    @Test
    fun `unlock logic respects best score`() {
        val ninja = Characters.byId("ninja")
        assertFalse(ninja.isUnlocked(4_999))
        assertTrue(ninja.isUnlocked(5_000))
        assertTrue(Characters.byId("clasico").isUnlocked(0))
        assertFalse(Characters.byId("robot").isUnlocked(49_999))
    }

    @Test
    fun `every character builds a valid 16x16 sprite`() {
        Characters.ALL.forEach { character ->
            val sprite = character.sprite
            assertEquals("${character.id} width", 16, sprite.width)
            assertEquals("${character.id} height", 16, sprite.height)
            sprite.pixels.forEach { row ->
                assertEquals("${character.id} row", 16, row.length)
            }
        }
    }

    @Test
    fun `overlays differ so each monkey looks unique`() {
        val distinct = Characters.ALL.map { it.sprite.pixels }.toSet()
        assertEquals(6, distinct.size)
    }

    @Test
    fun `byId falls back to the first character`() {
        assertEquals("clasico", Characters.byId("no-existe").id)
    }
}
