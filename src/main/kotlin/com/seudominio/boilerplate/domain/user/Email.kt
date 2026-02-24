package com.seudominio.boilerplate.domain.user

@JvmInline
value class Email(val value: String) {
    init {
        require(value.isNotBlank()) { "Email must not be blank" }
        require(value.contains("@")) { "Email '$value' is not a valid email address" }
    }
}
