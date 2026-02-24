package com.seudominio.boilerplate.application.user

import com.seudominio.boilerplate.domain.user.UserNotFoundException
import com.seudominio.boilerplate.domain.user.Email
import com.seudominio.boilerplate.domain.user.User
import com.seudominio.boilerplate.domain.user.UserId
import com.seudominio.boilerplate.domain.user.UserRepository
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.verify
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.time.Instant

class DeleteUserUseCaseTest {

    private val userRepository: UserRepository = mockk()
    private val useCase = DeleteUserUseCase(userRepository)

    private fun aUser(id: UserId) = User(
        id = id,
        email = Email("alice@example.com"),
        createdAt = Instant.now(),
        name = "Alice",
    )

    @Nested
    inner class WhenUserExists {
        @Test
        fun `should delete user by id`() {
            val id = UserId.generate()
            every { userRepository.findById(id) } returns aUser(id)
            every { userRepository.deleteById(id) } just runs

            useCase.execute(id.value)

            verify(exactly = 1) { userRepository.deleteById(id) }
        }
    }

    @Nested
    inner class WhenUserDoesNotExist {
        @Test
        fun `should throw UserNotFoundException without calling deleteById`() {
            val id = UserId.generate()
            every { userRepository.findById(id) } returns null

            assertThrows<UserNotFoundException> { useCase.execute(id.value) }
            verify(exactly = 0) { userRepository.deleteById(any()) }
        }
    }
}
