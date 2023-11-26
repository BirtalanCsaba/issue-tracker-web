package com.issue.tracker.infra.persistence.common

import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.MappedSuperclass
import java.io.Serializable

@MappedSuperclass
abstract class BaseEntity<ID : Serializable>(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: ID? = null
)