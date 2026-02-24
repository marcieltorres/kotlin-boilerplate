package com.seudominio.boilerplate.application.user

import com.seudominio.boilerplate.application.user.dto.UpdateUserCommand
import com.seudominio.boilerplate.application.user.dto.UserResult
import com.seudominio.boilerplate.domain.user.UserNotFoundException
import com.seudominio.boilerplate.domain.user.UserId
import com.seudominio.boilerplate.domain.user.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UpdateUserUseCase(
    private val userRepository: UserRepository,
) {
    @Transactional
    fun execute(command: UpdateUserCommand): UserResult {
        val userId = UserId(command.id)
        val user = userRepository.findById(userId)
            ?: throw UserNotFoundException(userId)

        user.changeName(command.name)  // aggregate enforces its own invariants

        return UserResult.from(userRepository.save(user))
    }
}
