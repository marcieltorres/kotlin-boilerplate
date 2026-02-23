package com.seudominio.boilerplate.api.user.mapper

import com.seudominio.boilerplate.api.user.request.CreateUserRequest
import com.seudominio.boilerplate.api.user.request.UpdateUserRequest
import com.seudominio.boilerplate.api.user.response.UserResponse
import com.seudominio.boilerplate.application.user.dto.CreateUserCommand
import com.seudominio.boilerplate.application.user.dto.UpdateUserCommand
import com.seudominio.boilerplate.application.user.dto.UserResult
import java.util.UUID

fun CreateUserRequest.toCommand(): CreateUserCommand =
    CreateUserCommand(email = email, name = name)

fun UpdateUserRequest.toCommand(id: UUID): UpdateUserCommand =
    UpdateUserCommand(id = id, name = name)

fun UserResult.toResponse(): UserResponse =
    UserResponse(id = id, email = email, name = name, createdAt = createdAt)