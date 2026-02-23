package com.seudominio.boilerplate.application.user.dto

import com.seudominio.boilerplate.domain.user.User
import java.time.Instant
import java.util.UUID

data class UserResult(
    val id: UUID,
    val email: String,
    val name: String,
    val createdAt: Instant,
) {
    companion object {
        fun from(user: User): UserResult = UserResult(
            id = user.id.value,         // unwrap UserId → UUID
            email = user.email.value,   // unwrap Email → String
            name = user.name,
            createdAt = user.createdAt,
        )
    }
}