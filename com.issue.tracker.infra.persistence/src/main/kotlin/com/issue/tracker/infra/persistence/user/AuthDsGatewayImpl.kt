package com.issue.tracker.infra.persistence.user

import com.issue.tracker.api.persistence.auth.AuthDsGateway
import com.issue.tracker.api.persistence.auth.SaveUserRequestModel
import com.issue.tracker.api.persistence.auth.UserDsResponseModel
import com.issue.tracker.infra.security.BCryptManager
import jakarta.ejb.Stateless
import jakarta.persistence.EntityManager
import jakarta.persistence.PersistenceContext

@Stateless
open class AuthDsGatewayImpl: AuthDsGateway {

    @PersistenceContext(unitName = "jpa")
    private lateinit var entityManager : EntityManager

    override fun existsByUsernameAndPassword(username: String, password: String): Boolean {
        val encryptedPassword = BCryptManager.encrypt(password)

        val queryString = "select exists(u) from UserEntity u where username=:username and password=:password"

        val query = entityManager.createQuery(queryString)
        query.setParameter("username", username)
        query.setParameter("password", encryptedPassword)

        return query.singleResult as Boolean
    }

    override fun findByUsername(username: String): UserDsResponseModel? {
        val queryString = "select u from UserEntity u where username=:username"

        val query = entityManager.createQuery(queryString)
        query.setParameter("username", username)
        val result = query.singleResult as UserEntity? ?: return null

        return UserDsResponseModel(
            id = result.id,
            username = result.username,
            password = result.password,
            firstName = result.firstName,
            lastName = result.lastName
        )
    }

    override fun save(saveUserRequestModel: SaveUserRequestModel): UserDsResponseModel? {
        val encryptedPassword = BCryptManager.encrypt(saveUserRequestModel.password)
        entityManager.persist(
            UserEntity(
                username = saveUserRequestModel.username,
                password = encryptedPassword,
                firstName = saveUserRequestModel.firstName,
                lastName = saveUserRequestModel.lastName,
            )
        )
        return findByUsername(username = saveUserRequestModel.username)
    }

    override fun findById(id: Long): UserDsResponseModel? {
        val queryString = "select u from UserEntity u where id=:id"
        val query = entityManager.createQuery(queryString)
        query.setParameter("id", id)

        val result = query.singleResult as UserEntity? ?: return null

        return UserDsResponseModel(
            id = result.id,
            username = result.username,
            password = result.password,
            firstName = result.firstName,
            lastName = result.lastName
        )
    }
}