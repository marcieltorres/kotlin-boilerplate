package com.seudominio.boilerplate.application.user

import com.seudominio.boilerplate.domain.user.Email
import com.seudominio.boilerplate.domain.user.User
import com.seudominio.boilerplate.domain.user.UserId
import com.seudominio.boilerplate.domain.user.UserRepository
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test
import java.time.Instant
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ListUsersUseCaseTest {

    private val userRepository: UserRepository = mockk()
    private val useCase = ListUsersUseCase(userRepository)

    private fun aUser(email: String) = User(
        id = UserId.generate(),
        email = Email(email),
        createdAt = Instant.now(),
        name = "Alice",
    )

    @Test
    fun `should return all users as UserResult list`() {
        every { userRepository.findAll() } returns listOf(
            aUser("a@example.com"),
            aUser("b@example.com"),
        )

        val results = useCase.execute()

        assertEquals(2, results.size)
    }

    @Test
    fun `should return empty list when no users exist`() {
        every { userRepository.findAll() } returns emptyList()
        assertTrue(useCase.execute().isEmpty())
    }
}