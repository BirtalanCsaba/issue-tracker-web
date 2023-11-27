package com.issue.tracker.api.auth;

import jakarta.ejb.Local;

@Local
public interface AuthInput {
    UserResponseModel register(RegisterRequestModel registerRequestModel);

    boolean login(LoginRequestModel loginRequestModel);
}
