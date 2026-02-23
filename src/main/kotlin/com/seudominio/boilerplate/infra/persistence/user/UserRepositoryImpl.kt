package com.seudominio.boilerplate.infra.persistence.user

import com.seudominio.boilerplate.domain.user.Email
import com.seudominio.boilerplate.domain.user.User
import com.seudominio.boilerplate.domain.user.UserId
import com.seudominio.boilerplate.domain.user.UserRepository
import org.springframework.stereotype.Repository

@Repository
class UserRepositoryImpl(
    private val jpaRepository: UserJpaRepository,
) : UserRepository {

    override fun save(user: User): User =
        jpaRepository.save(UserEntity(user)).toModel()

    override fun findById(id: UserId): User? =
        jpaRepository.findById(id.value).orElse(null)?.toModel()

    override fun findByEmail(email: Email): User? =
        jpaRepository.findByEmail(email.value)?.toModel()

    override fun findAll(): List<User> =
        jpaRepository.findAll().map { it.toModel() }

    override fun deleteById(id: UserId) =
        jpaRepository.deleteById(id.value)
}