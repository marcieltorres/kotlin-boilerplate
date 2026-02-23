package com.seudominio.boilerplate.api.user

import com.seudominio.boilerplate.api.user.mapper.toCommand
import com.seudominio.boilerplate.api.user.mapper.toResponse
import com.seudominio.boilerplate.api.user.request.CreateUserRequest
import com.seudominio.boilerplate.api.user.request.UpdateUserRequest
import com.seudominio.boilerplate.api.user.response.UserResponse
import com.seudominio.boilerplate.application.user.CreateUserUseCase
import com.seudominio.boilerplate.application.user.DeleteUserUseCase
import com.seudominio.boilerplate.application.user.GetUserUseCase
import com.seudominio.boilerplate.application.user.ListUsersUseCase
import com.seudominio.boilerplate.application.user.UpdateUserUseCase
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.util.UriComponentsBuilder
import java.util.UUID

@RestController
@RequestMapping("/api/v1/users")
class UserController(
    private val createUser: CreateUserUseCase,
    private val getUser: GetUserUseCase,
    private val listUsers: ListUsersUseCase,
    private val updateUser: UpdateUserUseCase,
    private val deleteUser: DeleteUserUseCase,
) {
    @PostMapping
    fun create(
        @Valid @RequestBody request: CreateUserRequest,
        uriBuilder: UriComponentsBuilder,
    ): ResponseEntity<UserResponse> {
        val result = createUser.execute(request.toCommand()).toResponse()
        val location = uriBuilder.path("/{id}").buildAndExpand(result.id).toUri()
        return ResponseEntity.created(location).body(result)
    }

    @GetMapping
    fun list(): ResponseEntity<List<UserResponse>> =
        ResponseEntity.ok(listUsers.execute().map { it.toResponse() })

    @GetMapping("/{id}")
    fun getById(@PathVariable id: UUID): ResponseEntity<UserResponse> =
        ResponseEntity.ok(getUser.execute(id).toResponse())

    @PutMapping("/{id}")
    fun update(
        @PathVariable id: UUID,
        @Valid @RequestBody request: UpdateUserRequest,
    ): ResponseEntity<UserResponse> =
        ResponseEntity.ok(updateUser.execute(request.toCommand(id)).toResponse())

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: UUID): ResponseEntity<Void> {
        deleteUser.execute(id)
        return ResponseEntity.noContent().build()
    }
}