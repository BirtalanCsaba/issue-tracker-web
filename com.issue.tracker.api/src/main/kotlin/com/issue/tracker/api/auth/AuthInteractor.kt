package com.issue.tracker.api.auth

import com.issue.tracker.api.persistence.auth.AuthDsGateway
import com.issue.tracker.api.persistence.auth.SaveUserRequestModel

class AuthInteractor(
    private val authDsGateway: AuthDsGateway
) : AuthInput {
    override fun register(registerRequestModel: RegisterRequestModel): UserResponseModel {
        if (authDsGateway.findByUsername(registerRequestModel.username) != null) {
            throw UserAlreadyExistException("User with the same username already exists")
        }

        val createdUser = authDsGateway.save(
            SaveUserRequestModel(
            username = registerRequestModel.username,
            password = registerRequestModel.password,
            firstName = registerRequestModel.firstName,
            lastName = registerRequestModel.lastName,
        )
        ) ?: throw UserCreationFailedException("User creation failed")

        return UserResponseModel(
            id = createdUser.id,
            username = createdUser.username,
            password = createdUser.password,
            firstName = createdUser.firstName,
            lastName = createdUser.lastName,
        )
    }

    override fun login(loginRequestModel: LoginRequestModel): Boolean {
        return authDsGateway.existsByUsernameAndPassword(
            username = loginRequestModel.username,
            password = loginRequestModel.password,
        )
    }
}