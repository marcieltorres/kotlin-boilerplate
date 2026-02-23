package com.seudominio.boilerplate.infra.persistence.user

import com.seudominio.boilerplate.domain.user.Email
import com.seudominio.boilerplate.domain.user.User
import com.seudominio.boilerplate.domain.user.UserId
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.Instant
import java.util.UUID

@Entity
@Table(name = "users")
class UserEntity(
    @Id
    val id: UUID,

    @Column(nullable = false, unique = true, length = 255)
    val email: String,

    @Column(nullable = false, length = 100)
    val name: String,

    @Column(nullable = false, updatable = false)
    val createdAt: Instant,
) {
    // Required by JPA spec (used by Hibernate reflectively)
    protected constructor() : this(
        id = UUID.randomUUID(),
        email = "",
        name = "",
        createdAt = Instant.now(),
    )

    // domain → JPA: unwrap value objects to JPA primitives
    constructor(user: User) : this(
        id = user.id.value,
        email = user.email.value,
        name = user.name,
        createdAt = user.createdAt,
    )

    // JPA → domain: wrap primitives back into value objects
    fun toModel(): User = User(
        id = UserId(id),
        email = Email(email),   // re-validates format; corrupt DB data will throw
        createdAt = createdAt,
        name = name,
    )
}