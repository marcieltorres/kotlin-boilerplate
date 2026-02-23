package com.seudominio.boilerplate.domain.user

interface UserRepository {
    fun save(user: User): User
    fun findById(id: UserId): User?
    fun findByEmail(email: Email): User?
    fun findAll(): List<User>
    fun deleteById(id: UserId)
}