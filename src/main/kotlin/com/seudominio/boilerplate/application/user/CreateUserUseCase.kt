package com.seudominio.boilerplate.application.user

import com.seudominio.boilerplate.application.user.dto.CreateUserCommand
import com.seudominio.boilerplate.application.user.dto.UserResult
import com.seudominio.boilerplate.domain.user.DuplicateEmailException
import com.seudominio.boilerplate.domain.user.Email
import com.seudominio.boilerplate.domain.user.User
import com.seudominio.boilerplate.domain.user.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CreateUserUseCase(
    private val userRepository: UserRepository,
) {
    @Transactional
    fun execute(command: CreateUserCommand): UserResult {
        val email = Email(command.email)  // validates format; throws IllegalArgumentException if invalid

        if (userRepository.findByEmail(email) != null) {
            throw DuplicateEmailException(email)
        }

        val user = User.create(email = email, name = command.name)

        return UserResult.from(userRepository.save(user))
    }
}
