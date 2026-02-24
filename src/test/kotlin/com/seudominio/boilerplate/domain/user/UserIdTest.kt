package com.seudominio.boilerplate.domain.user

import org.junit.jupiter.api.Test
import java.util.UUID
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

class UserIdTest {

    @Test
    fun `generate should produce a valid non-null UserId`() {
        val id = UserId.generate()
        // value class — just verify it wraps a real UUID
        assertEquals(36, id.value.toString().length)
    }

    @Test
    fun `two generated ids should be unique`() {
        assertNotEquals(UserId.generate(), UserId.generate())
    }

    @Test
    fun `of should parse a valid UUID string`() {
        val raw = UUID.randomUUID()
        assertEquals(UserId(raw), UserId.of(raw.toString()))
    }

    @Test
    fun `of should throw for an invalid UUID string`() {
        org.junit.jupiter.api.assertThrows<IllegalArgumentException> {
            UserId.of("not-a-uuid")
        }
    }
}
