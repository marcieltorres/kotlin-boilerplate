package com.seudominio.boilerplate.application.user

import com.seudominio.boilerplate.domain.user.UserNotFoundException
import com.seudominio.boilerplate.domain.user.UserId
import com.seudominio.boilerplate.domain.user.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
class DeleteUserUseCase(
    private val userRepository: UserRepository,
) {
    @Transactional
    fun execute(id: UUID) {
        val userId = UserId(id)
        userRepository.findById(userId)
            ?: throw UserNotFoundException(userId)
        userRepository.deleteById(userId)
    }
}