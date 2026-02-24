package com.seudominio.boilerplate.application.user

import com.seudominio.boilerplate.application.user.dto.UserResult
import com.seudominio.boilerplate.domain.user.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ListUsersUseCase(
    private val userRepository: UserRepository,
) {
    @Transactional(readOnly = true)
    fun execute(): List<UserResult> =
        userRepository.findAll().map { UserResult.from(it) }
}
