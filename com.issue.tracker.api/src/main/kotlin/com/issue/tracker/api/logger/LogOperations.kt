package com.issue.tracker.api.logger

interface LogOperations {
    fun print()

    fun constructString(): String
}