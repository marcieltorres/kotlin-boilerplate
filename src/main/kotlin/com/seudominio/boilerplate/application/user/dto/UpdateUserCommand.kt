package com.seudominio.boilerplate.application.user.dto

import java.util.UUID

data class UpdateUserCommand(
    val id: UUID,
    val name: String,
)