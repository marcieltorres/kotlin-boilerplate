package com.seudominio.boilerplate.application.user

import com.seudominio.boilerplate.domain.user.UserNotFoundException
import com.seudominio.boilerplate.domain.user.Email
import com.seudominio.boilerplate.domain.user.User
import com.seudominio.boilerplate.domain.user.UserId
import com.seudominio.boilerplate.domain.user.UserRepository
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.time.Instant
import kotlin.test.assertEquals

class GetUserUseCaseTest {

    private val userRepository: UserRepository = mockk()
    private val useCase = GetUserUseCase(userRepository)

    private val existingId = UserId.generate()
    private val existingUser = User(
        id = existingId,
        email = Email("alice@example.com"),
        createdAt = Instant.now(),
        name = "Alice",
    )

    @Nested
    inner class WhenUserExists {
        @Test
        fun `should return UserResult`() {
            every { userRepository.findById(existingId) } returns existingUser

            val result = useCase.execute(existingId.value)

            assertEquals(existingId.value, result.id)
            assertEquals("alice@example.com", result.email)
        }
    }

    @Nested
    inner class WhenUserDoesNotExist {
        @Test
        fun `should throw UserNotFoundException`() {
            val missingId = UserId.generate()
            every { userRepository.findById(missingId) } returns null

            assertThrows<UserNotFoundException> { useCase.execute(missingId.value) }
        }
    }
}