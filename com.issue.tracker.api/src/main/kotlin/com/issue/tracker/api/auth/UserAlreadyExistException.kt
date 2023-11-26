package com.issue.tracker.api.auth

import com.issue.tracker.api.ApiException

class UserAlreadyExistException: ApiException {
    constructor(message: String) : super(message)
    constructor(message: String, reason: String) : super(message, reason)
}