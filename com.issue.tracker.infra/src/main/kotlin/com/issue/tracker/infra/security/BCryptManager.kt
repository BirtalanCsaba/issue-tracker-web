package com.issue.tracker.infra.security

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

object BCryptManager {
    private val encoder = BCryptPasswordEncoder()

    fun encrypt(value: String): String {
        return encoder.encode(value)
    }
}