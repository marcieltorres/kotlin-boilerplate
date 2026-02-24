package com.seudominio.boilerplate.api.shared

import java.time.Instant

data class ApiErrorResponse(
    val status: Int,
    val error: String,
    val message: String,
    val path: String,
    val timestamp: Instant = Instant.now(),
    val fieldErrors: Map<String, String>? = null,
)
