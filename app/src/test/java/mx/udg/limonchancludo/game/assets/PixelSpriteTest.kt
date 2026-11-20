package mx.udg.limonchancludo.game.assets

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class PixelSpriteTest {

    @Test
    fun `all sprites have consistent row widths`() {
        Sprites.all.forEach { sprite ->
            val width = sprite.pixels.first().length
            sprite.pixels.forEachIndexed { i, row ->
                assertEquals(
                    "${sprite.name}: fila $i",
                    width,
                    row.length
                )
            }
        }
    }

    @Test
    fun `all sprite chars belong to the palette`() {
        Sprites.all.forEach { sprite ->
            sprite.pixels.forEach { row ->
                row.forEach { ch ->
                    assertTrue(
                        "${sprite.name}: '$ch' fuera de paleta",
                        ch == PixelSprite.TRANSPARENT || ch in sprite.palette
                    )
                }
            }
        }
    }

    @Test
    fun `tile sprites are 16x16`() {
        listOf(
            Sprites.lemonDot,
            Sprites.powerLemon,
            Sprites.gateDoor,
            Sprites.heart,
            Sprites.lock
        ).forEach { sprite ->
            assertEquals("${sprite.name} width", 16, sprite.width)
            assertEquals("${sprite.name} height", 16, sprite.height)
        }
        assertEquals(8, Sprites.wallBrick.width)
        assertEquals(8, Sprites.wallBrick.height)
    }

    @Test
    fun `wall sprite is fully opaque`() {
        Sprites.wallBrick.pixels.forEach { row ->
            row.forEach { ch ->
                assertTrue("muro con transparencia: '$ch'", ch != PixelSprite.TRANSPARENT)
            }
        }
    }

    @Test(expected = IllegalArgumentException::class)
    fun `invalid sprite rows are rejected`() {
        PixelSprite(
            name = "roto",
            pixels = listOf("....", "..."),
            palette = emptyMap()
        )
    }
}
