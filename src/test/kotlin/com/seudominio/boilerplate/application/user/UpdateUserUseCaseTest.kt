package com.seudominio.boilerplate.application.user

import com.seudominio.boilerplate.application.user.dto.UpdateUserCommand
import com.seudominio.boilerplate.domain.user.UserNotFoundException
import com.seudominio.boilerplate.domain.user.Email
import com.seudominio.boilerplate.domain.user.User
import com.seudominio.boilerplate.domain.user.UserId
import com.seudominio.boilerplate.domain.user.UserRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.time.Instant
import kotlin.test.assertEquals

class UpdateUserUseCaseTest {

    private val userRepository: UserRepository = mockk()
    private val useCase = UpdateUserUseCase(userRepository)

    private val userId = UserId.generate()
    private val existingUser = User(
        id = userId,
        email = Email("alice@example.com"),
        createdAt = Instant.now(),
        name = "Alice",
    )

    @Nested
    inner class WhenUserExists {
        @Test
        fun `should call changeName on aggregate and persist`() {
            every { userRepository.findById(userId) } returns existingUser
            every { userRepository.save(existingUser) } returns existingUser

            val result = useCase.execute(UpdateUserCommand(id = userId.value, name = "Alicia"))

            assertEquals("Alicia", result.name)
            // Verifies the aggregate's own method was honoured, not a copy() bypass
            verify(exactly = 1) { userRepository.save(existingUser) }
        }
    }

    @Nested
    inner class WhenUserDoesNotExist {
        @Test
        fun `should throw UserNotFoundException`() {
            val missingId = UserId.generate()
            every { userRepository.findById(missingId) } returns null

            assertThrows<UserNotFoundException> {
                useCase.execute(UpdateUserCommand(id = missingId.value, name = "Bob"))
            }
        }
    }
}