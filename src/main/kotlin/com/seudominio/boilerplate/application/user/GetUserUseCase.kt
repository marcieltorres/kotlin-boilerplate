package com.seudominio.boilerplate.application.user

import com.seudominio.boilerplate.application.user.dto.UserResult
import com.seudominio.boilerplate.domain.user.UserNotFoundException
import com.seudominio.boilerplate.domain.user.UserId
import com.seudominio.boilerplate.domain.user.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
class GetUserUseCase(
    private val userRepository: UserRepository,
) {
    @Transactional(readOnly = true)
    fun execute(id: UUID): UserResult {
        val userId = UserId(id)
        val user = userRepository.findById(userId)
            ?: throw UserNotFoundException(userId)
        return UserResult.from(user)
    }
}
