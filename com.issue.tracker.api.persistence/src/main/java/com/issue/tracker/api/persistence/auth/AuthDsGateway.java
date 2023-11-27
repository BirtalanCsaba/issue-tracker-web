package com.issue.tracker.api.persistence.auth;

import jakarta.ejb.Remote;

@Remote
public interface AuthDsGateway {
    boolean existsByUsernameAndPassword(String username, String password);

    UserDsResponseModel findByUsername(String username);

    UserDsResponseModel save(SaveUserRequestModel saveUserRequestModel);

    UserDsResponseModel findById(Long id);
}
