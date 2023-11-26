package com.issue.tracker.api.persistence.auth

class SaveUserRequestModel(
    var firstName: String,
    var lastName: String,
    var username: String,
    var password: String,
)