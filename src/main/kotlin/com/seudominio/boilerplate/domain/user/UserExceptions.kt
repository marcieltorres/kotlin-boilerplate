package com.seudominio.boilerplate.domain.user

import com.seudominio.boilerplate.domain.shared.DomainException

class UserNotFoundException(id: UserId) :
    DomainException("User not found with id: ${id.value}")

class DuplicateEmailException(email: Email) :
    DomainException("A user with email '${email.value}' already exists")