package com.issue.tracker.infra.persistence.user

import com.issue.tracker.infra.persistence.common.BaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Table

@Entity
@Table(name = "user")
open class UserEntity : BaseEntity<Long> {
    @Column(nullable = false, length = 60, unique = true)
    var username: String? = null

    @Column(nullable = false, length = 255)
    var password: String? = null

    @Column(nullable = false, length = 60)
    var firstName: String? = null

    @Column(nullable = false, length = 60)
    var lastName: String? = null

    constructor()

    constructor(
        username: String,
        password: String,
        firstName: String,
        lastName: String,
    ) {
        this.username = username
        this.password = password
        this.firstName = firstName
        this.lastName = lastName
    }
}