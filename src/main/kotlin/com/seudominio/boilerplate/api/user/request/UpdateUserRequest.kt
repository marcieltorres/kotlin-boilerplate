package com.seudominio.boilerplate.api.user.request

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class UpdateUserRequest(
    @field:NotBlank(message = "Name must not be blank")
    @field:Size(max = 100, message = "Name must not exceed 100 characters")
    val name: String,
)
