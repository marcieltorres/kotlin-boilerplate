package com.seudominio.boilerplate.domain.user

import java.time.Instant

/**
 * User aggregate root.
 *
 * Entity: equality and identity are based solely on [id].
 * Two User instances with the same [id] but different [name] are the
 * same entity at different points in time — they must be equal.
 */
class User(
    val id: UserId,
    val email: Email,
    val createdAt: Instant,
    name: String,
) {
    var name: String = name
        private set

    init {
        require(name.isNotBlank()) { "Name must not be blank" }
        require(name.length <= 100) { "Name must not exceed 100 characters" }
    }

    /** Domain behaviour: rename this user. Enforces the same invariants as construction. */
    fun changeName(newName: String) {
        require(newName.isNotBlank()) { "Name must not be blank" }
        require(newName.length <= 100) { "Name must not exceed 100 characters" }
        name = newName
    }

    // Entity equality: identity only.
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is User) return false
        return id == other.id
    }

    override fun hashCode(): Int = id.hashCode()

    override fun toString(): String = "User(id=$id, email=$email, name='$name')"

    companion object {
        /**
         * Factory for creating a *new* user. Generates a fresh [UserId] and
         * captures [createdAt] from the system clock.
         * Use the primary constructor only for re-hydration from persistence.
         */
        fun create(email: Email, name: String): User = User(
            id = UserId.generate(),
            email = email,
            createdAt = Instant.now(),
            name = name,
        )
    }
}
