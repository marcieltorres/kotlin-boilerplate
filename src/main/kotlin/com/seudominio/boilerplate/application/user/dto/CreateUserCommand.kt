package com.seudominio.boilerplate.application.user.dto

data class CreateUserCommand(
    val email: String,
    val name: String,
)
