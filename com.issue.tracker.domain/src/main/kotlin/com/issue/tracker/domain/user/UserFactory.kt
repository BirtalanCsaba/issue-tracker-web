package com.issue.tracker.domain.user

interface UserFactory {
    fun createUser(firstName: String, lastName: String, username: String, password: String): User
}