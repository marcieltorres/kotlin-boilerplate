package com.seudominio.boilerplate.domain.user

import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals

class EmailTest {

    @Nested
    inner class ValidEmail {
        @Test
        fun `should accept a well-formed email address`() {
            val email = Email("alice@example.com")
            assertEquals("alice@example.com", email.value)
        }
    }

    @Nested
    inner class InvalidEmail {
        @Test
        fun `should throw when email is blank`() {
            assertThrows<IllegalArgumentException> { Email("") }
        }

        @Test
        fun `should throw when email has no at-sign`() {
            assertThrows<IllegalArgumentException> { Email("notanemail") }
        }
    }
}