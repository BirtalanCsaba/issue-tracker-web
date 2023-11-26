package com.issue.tracker.api

open class ApiException : RuntimeException {
    var reason: String? = null

    constructor(message: String) : super(message)

    constructor(message: String, reason: String) : super(message) {
        this.reason = reason
    }
}