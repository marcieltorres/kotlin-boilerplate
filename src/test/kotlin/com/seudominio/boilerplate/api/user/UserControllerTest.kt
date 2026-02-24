package com.seudominio.boilerplate.api.user

import com.seudominio.boilerplate.application.user.CreateUserUseCase
import com.seudominio.boilerplate.application.user.DeleteUserUseCase
import com.seudominio.boilerplate.application.user.GetUserUseCase
import com.seudominio.boilerplate.application.user.ListUsersUseCase
import com.seudominio.boilerplate.application.user.UpdateUserUseCase
import com.seudominio.boilerplate.application.user.dto.UserResult
import com.seudominio.boilerplate.domain.user.DuplicateEmailException
import com.seudominio.boilerplate.domain.user.Email
import com.seudominio.boilerplate.domain.user.UserNotFoundException
import com.seudominio.boilerplate.domain.user.UserId
import com.seudominio.boilerplate.api.user.request.CreateUserRequest
import com.seudominio.boilerplate.api.user.request.UpdateUserRequest
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.verify
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.http.HttpStatus
import org.springframework.web.util.UriComponentsBuilder
import java.time.Instant
import java.util.UUID
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class UserControllerTest {

    private val createUserUseCase: CreateUserUseCase = mockk()
    private val getUserUseCase: GetUserUseCase = mockk()
    private val listUsersUseCase: ListUsersUseCase = mockk()
    private val updateUserUseCase: UpdateUserUseCase = mockk()
    private val deleteUserUseCase: DeleteUserUseCase = mockk()

    private val controller = UserController(
        createUserUseCase,
        getUserUseCase,
        listUsersUseCase,
        updateUserUseCase,
        deleteUserUseCase,
    )

    private val userId = UUID.randomUUID()
    private val userResult = UserResult(
        id = userId,
        email = "alice@example.com",
        name = "Alice",
        createdAt = Instant.parse("2024-01-01T00:00:00Z"),
    )

    @Nested
    inner class CreateUser {
        private val uriBuilder = UriComponentsBuilder.fromPath("/api/v1/users")

        @Test
        fun `should return 201 with body and Location header`() {
            every { createUserUseCase.execute(any()) } returns userResult

            val response = controller.create(CreateUserRequest(name = "Alice", email = "alice@example.com"), uriBuilder)

            assertEquals(HttpStatus.CREATED, response.statusCode)
            assertNotNull(response.headers.location)
            assertEquals("alice@example.com", response.body?.email)
            assertEquals("Alice", response.body?.name)
        }

        @Test
        fun `should propagate DuplicateEmailException`() {
            every { createUserUseCase.execute(any()) } throws DuplicateEmailException(Email("alice@example.com"))

            assertThrows<DuplicateEmailException> {
                controller.create(CreateUserRequest(name = "Alice", email = "alice@example.com"), uriBuilder)
            }
        }
    }

    @Nested
    inner class ListUsers {
        @Test
        fun `should return 200 with list of users`() {
            every { listUsersUseCase.execute() } returns listOf(userResult)

            val response = controller.list()

            assertEquals(HttpStatus.OK, response.statusCode)
            assertEquals(1, response.body?.size)
            assertEquals("alice@example.com", response.body?.first()?.email)
        }

        @Test
        fun `should return 200 with empty list`() {
            every { listUsersUseCase.execute() } returns emptyList()

            val response = controller.list()

            assertEquals(HttpStatus.OK, response.statusCode)
            assertEquals(0, response.body?.size)
        }
    }

    @Nested
    inner class GetUser {
        @Test
        fun `should return 200 with user body`() {
            every { getUserUseCase.execute(userId) } returns userResult

            val response = controller.getById(userId)

            assertEquals(HttpStatus.OK, response.statusCode)
            assertEquals("alice@example.com", response.body?.email)
        }

        @Test
        fun `should propagate UserNotFoundException`() {
            every { getUserUseCase.execute(userId) } throws UserNotFoundException(UserId(userId))

            assertThrows<UserNotFoundException> { controller.getById(userId) }
        }
    }

    @Nested
    inner class UpdateUser {
        @Test
        fun `should return 200 with updated user`() {
            every { updateUserUseCase.execute(any()) } returns userResult.copy(name = "Alicia")

            val response = controller.update(userId, UpdateUserRequest(name = "Alicia"))

            assertEquals(HttpStatus.OK, response.statusCode)
            assertEquals("Alicia", response.body?.name)
        }

        @Test
        fun `should propagate UserNotFoundException`() {
            every { updateUserUseCase.execute(any()) } throws UserNotFoundException(UserId(userId))

            assertThrows<UserNotFoundException> { controller.update(userId, UpdateUserRequest(name = "Bob")) }
        }
    }

    @Nested
    inner class DeleteUser {
        @Test
        fun `should return 204 no content`() {
            every { deleteUserUseCase.execute(userId) } just runs

            val response = controller.delete(userId)

            assertEquals(HttpStatus.NO_CONTENT, response.statusCode)
            verify(exactly = 1) { deleteUserUseCase.execute(userId) }
        }

        @Test
        fun `should propagate UserNotFoundException`() {
            every { deleteUserUseCase.execute(userId) } throws UserNotFoundException(UserId(userId))

            assertThrows<UserNotFoundException> { controller.delete(userId) }
        }
    }
}
