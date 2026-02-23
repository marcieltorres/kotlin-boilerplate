package com.seudominio.boilerplate.domain.user

import java.util.UUID

@JvmInline
value class UserId(val value: UUID) {

    companion object {
        fun generate(): UserId = UserId(UUID.randomUUID())
        fun of(value: String): UserId = UserId(UUID.fromString(value))
    }
}