package com.kychnoo.gamevault.data.model.request

import org.junit.Assert.assertEquals
import org.junit.Test

class GameSearchParametersTest {

    @Test
    fun `toQueryMap should include required fields`() {
        val params = GameSearchParameters(search = "Cyberpunk")
        val map = params.toQueryMap()

        assertEquals("Cyberpunk", map["search"])
        assertEquals("1", map["page"])
        assertEquals("20", map["page_size"])
    }

    @Test
    fun `toQueryMap should include optional fields when provided`() {
        val params = GameSearchParameters(
            search = "The Witcher",
            genres = "action,rpg",
            ordering = "-released"
        )
        val map = params.toQueryMap()

        assertEquals("The Witcher", map["search"])
        assertEquals("action,rpg", map["genres"])
        assertEquals("-released", map["ordering"])
    }

    @Test
    fun `toQueryMap should exclude null optional fields`() {
        val params = GameSearchParameters(search = "Mario")
        val map = params.toQueryMap()

        assertEquals(3, map.size)
        assertEquals(false, map.containsKey("genres"))
        assertEquals(false, map.containsKey("platforms"))
    }
}
