package com.issue.tracker.domain

class ValidationException : DomainException {
    constructor(): super("Validation exception")

    constructor(message: String) : super(message)
}