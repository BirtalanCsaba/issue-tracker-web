package com.issue.tracker.api.persistence.auth

interface AuthDsGateway {
    fun existsByUsernameAndPassword(username: String, password: String): Boolean

    fun findByUsername(username: String): UserDsResponseModel?

    fun save(saveUserRequestModel: SaveUserRequestModel): UserDsResponseModel?

    fun findById(id: Long): UserDsResponseModel?
}