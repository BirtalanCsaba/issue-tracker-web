package com.issue.tracker.api.persistence.auth;

import jakarta.ejb.Remote;

@Remote
public interface AuthDsGateway {
    boolean existsByUsernameAndPassword(String username, String password);

    UserDsCompleteResponseModel findByUsername(String username);

    UserDsCompleteResponseModel save(SaveUserRequestModel saveUserRequestModel);

    UserDsCompleteResponseModel findById(Long id);

    UserDsCompleteResponseModel findByEmail(String email);
}
