package com.seudominio.boilerplate.domain.user

import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.time.Instant
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

class UserTest {

    private fun aUser(
        id: UserId = UserId.generate(),
        email: Email = Email("alice@example.com"),
        name: String = "Alice",
    ) = User(id = id, email = email, createdAt = Instant.now(), name = name)

    @Nested
    inner class Construction {
        @Test
        fun `should create user with given fields`() {
            val user = aUser(name = "Alice", email = Email("alice@example.com"))
            assertEquals("Alice", user.name)
            assertEquals("alice@example.com", user.email.value)
        }

        @Test
        fun `should throw when name is blank`() {
            assertThrows<IllegalArgumentException> { aUser(name = "  ") }
        }

        @Test
        fun `should throw when name exceeds 100 characters`() {
            assertThrows<IllegalArgumentException> { aUser(name = "A".repeat(101)) }
        }
    }

    @Nested
    inner class Factory {
        @Test
        fun `create should assign a generated id and current timestamp`() {
            val before = Instant.now()
            val user = User.create(email = Email("bob@example.com"), name = "Bob")
            val after = Instant.now()

            assertNotEquals(UserId.generate(), user.id) // different instance, just checking type
            assert(!user.createdAt.isBefore(before))
            assert(!user.createdAt.isAfter(after))
        }
    }

    @Nested
    inner class Behaviour {
        @Test
        fun `changeName should update the name`() {
            val user = aUser(name = "Alice")
            user.changeName("Alicia")
            assertEquals("Alicia", user.name)
        }

        @Test
        fun `changeName should throw when new name is blank`() {
            val user = aUser()
            assertThrows<IllegalArgumentException> { user.changeName("") }
        }

        @Test
        fun `changeName should throw when new name exceeds 100 characters`() {
            val user = aUser()
            assertThrows<IllegalArgumentException> { user.changeName("A".repeat(101)) }
        }
    }

    @Nested
    inner class EntityEquality {
        /**
         * This is the critical DDD invariant: same Entity at different points in time.
         * A data class would fail this test because it compares all fields.
         * A proper Entity compares only identity (id).
         */
        @Test
        fun `should be equal to itself after changeName — same identity, different state`() {
            val id = UserId.generate()
            val user = aUser(id = id, name = "Alice")
            user.changeName("Alicia")

            val rehydrated = aUser(id = id, name = "Alice") // original name, same id
            assertEquals(user, rehydrated)
        }

        @Test
        fun `should consider users with same id equal`() {
            val id = UserId.generate()
            assertEquals(aUser(id = id), aUser(id = id))
        }

        @Test
        fun `should consider users with different ids unequal`() {
            assertNotEquals(aUser(), aUser())
        }

        @Test
        fun `hashCode should be consistent with equals`() {
            val id = UserId.generate()
            val a = aUser(id = id)
            val b = aUser(id = id)
            assertEquals(a.hashCode(), b.hashCode())
        }
    }
}
