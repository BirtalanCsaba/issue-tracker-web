package com.issue.tracker.api.auth

interface AuthInput {
    fun register(registerRequestModel: RegisterRequestModel): UserResponseModel

    fun login(loginRequestModel: LoginRequestModel): Boolean
}