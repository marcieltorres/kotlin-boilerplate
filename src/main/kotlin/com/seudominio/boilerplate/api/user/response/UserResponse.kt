package com.seudominio.boilerplate.api.user.response

import java.time.Instant
import java.util.UUID

data class UserResponse(
    val id: UUID,
    val email: String,
    val name: String,
    val createdAt: Instant,
)
