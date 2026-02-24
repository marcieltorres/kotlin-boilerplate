package com.seudominio.boilerplate.infra.persistence.user

import com.seudominio.boilerplate.domain.user.Email
import com.seudominio.boilerplate.domain.user.User
import com.seudominio.boilerplate.domain.user.UserId
import com.seudominio.boilerplate.infra.TestcontainersConfig
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.test.context.ActiveProfiles

import org.springframework.dao.DataIntegrityViolationException
import java.time.Instant
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ActiveProfiles("test")
@Import(TestcontainersConfig::class)
class UserRepositoryImplTest {

    @Autowired
    lateinit var repository: UserRepositoryImpl

    @Autowired
    lateinit var jpaRepository: UserJpaRepository

    private fun aUser(
        email: String = "alice@example.com",
        name: String = "Alice",
    ) = User(
        id = UserId.generate(),
        email = Email(email),
        createdAt = Instant.now(),
        name = name,
    )

    @BeforeEach
    fun cleanUp() {
        jpaRepository.deleteAll()
    }

    @Nested
    inner class Save {
        @Test
        fun `should persist and return a user`() {
            val user = aUser()
            val saved = repository.save(user)
            assertEquals(user.id, saved.id)
            assertEquals(user.email, saved.email)
        }

        @Test
        fun `should throw on duplicate email`() {
            repository.save(aUser(email = "dup@example.com"))
            assertThrows<DataIntegrityViolationException> {
                repository.save(aUser(email = "dup@example.com"))
            }
        }
    }

    @Nested
    inner class FindById {
        @Test
        fun `should return user when id exists`() {
            val user = repository.save(aUser())
            val found = repository.findById(user.id)
            assertNotNull(found)
            assertEquals(user.id, found.id)
        }

        @Test
        fun `should return null when id does not exist`() {
            assertNull(repository.findById(UserId.generate()))
        }
    }

    @Nested
    inner class FindByEmail {
        @Test
        fun `should return user when email exists`() {
            repository.save(aUser(email = "find@example.com"))
            assertNotNull(repository.findByEmail(Email("find@example.com")))
        }

        @Test
        fun `should return null when email does not exist`() {
            assertNull(repository.findByEmail(Email("ghost@example.com")))
        }
    }

    @Nested
    inner class FindAll {
        @Test
        fun `should return all saved users`() {
            repository.save(aUser(email = "a@example.com"))
            repository.save(aUser(email = "b@example.com"))
            assertEquals(2, repository.findAll().size)
        }

        @Test
        fun `should return empty list when no users exist`() {
            assertTrue(repository.findAll().isEmpty())
        }
    }

    @Nested
    inner class DeleteById {
        @Test
        fun `should remove user by id`() {
            val user = repository.save(aUser())
            repository.deleteById(user.id)
            assertNull(repository.findById(user.id))
        }
    }
}
