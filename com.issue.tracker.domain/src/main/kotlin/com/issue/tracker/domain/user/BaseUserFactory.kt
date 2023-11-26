package com.issue.tracker.domain.user

import com.issue.tracker.domain.ValidationException

class BaseUserFactory: UserFactory {
    override fun createUser(firstName: String, lastName: String, username: String, password: String): User {
        sanitizeUsername(username)
        sanitizePassword(password)
        sanitizeName(firstName)
        sanitizeName(lastName)
        validateUsername(username)
        validatePassword(password)
        validateName(firstName)
        validateName(lastName)

        return User(username, password)
    }

    private fun validateUsername(username: String) {
        // should not contain spaces
        if (username.matches(Regex("^\\S+$"))) {
            throw ValidationException("Username should not be empty or contain spaces")
        }
    }

    private fun validatePassword(password: String) {
        // should not contain spaces
        if (password.matches(Regex("^\\S+$"))) {
            throw ValidationException("Password should not be empty or contain spaces")
        }
    }

    private fun validateName(name: String) {
        if (name.isEmpty()) {
            throw ValidationException("Name is empty")
        }
    }

    private fun sanitizeName(name: String) {
        name.trim()
    }

    private fun sanitizeUsername(username: String) {
        username.trim()
    }

    private fun sanitizePassword(password: String) {
        password.trim()
    }

}