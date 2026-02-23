package com.seudominio.boilerplate.application.user

import com.seudominio.boilerplate.application.user.dto.CreateUserCommand
import com.seudominio.boilerplate.domain.user.DuplicateEmailException
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

class CreateUserUseCaseTest {

    private val userRepository: UserRepository = mockk()
    private val useCase = CreateUserUseCase(userRepository)

    @Nested
    inner class WhenEmailIsNew {
        @Test
        fun `should create and return user result`() {
            val command = CreateUserCommand(email = "alice@example.com", name = "Alice")
            every { userRepository.findByEmail(Email("alice@example.com")) } returns null
            every { userRepository.save(any()) } answers { firstArg() }

            val result = useCase.execute(command)

            assertEquals("alice@example.com", result.email)
            assertEquals("Alice", result.name)
            verify(exactly = 1) { userRepository.save(any()) }
        }
    }

    @Nested
    inner class WhenEmailAlreadyExists {
        @Test
        fun `should throw DuplicateEmailException`() {
            val command = CreateUserCommand(email = "dup@example.com", name = "Alice")
            val existing = User(
                id = UserId.generate(),
                email = Email("dup@example.com"),
                createdAt = Instant.now(),
                name = "Someone",
            )
            every { userRepository.findByEmail(Email("dup@example.com")) } returns existing

            assertThrows<DuplicateEmailException> { useCase.execute(command) }
            verify(exactly = 0) { userRepository.save(any()) }
        }
    }

    @Nested
    inner class WhenEmailIsInvalid {
        @Test
        fun `should throw IllegalArgumentException for malformed email`() {
            val command = CreateUserCommand(email = "notanemail", name = "Alice")
            // Email validation in the use case throws before any repository call
            assertThrows<IllegalArgumentException> { useCase.execute(command) }
        }
    }
}