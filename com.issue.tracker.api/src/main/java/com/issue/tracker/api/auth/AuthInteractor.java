package com.issue.tracker.api.auth;

import com.issue.tracker.api.persistence.auth.AuthDsGateway;
import com.issue.tracker.api.persistence.auth.SaveUserRequestModel;
import com.issue.tracker.api.persistence.auth.UserDsResponseModel;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;

@Stateless
public class AuthInteractor implements AuthInput {

    @EJB
    private AuthDsGateway authDsGateway;

    @Override
    public UserResponseModel register(RegisterRequestModel registerRequestModel) {
        if (authDsGateway.findByUsername(registerRequestModel.getUsername()) != null) {
            throw new UserAlreadyExistException("User with the same username already exists");
        }

        SaveUserRequestModel saveUserRequestModel = new SaveUserRequestModel(
                registerRequestModel.getUsername(),
                registerRequestModel.getPassword(),
                registerRequestModel.getFirstName(),
                registerRequestModel.getLastName()
        );

        UserDsResponseModel createdUser = authDsGateway.save(saveUserRequestModel);

        if (createdUser == null) {
            throw new UserCreationFailedException("User creation failed");
        }

        return new UserResponseModel(
                createdUser.getId(),
                createdUser.getUsername(),
                createdUser.getPassword(),
                createdUser.getFirstName(),
                createdUser.getLastName()
        );
    }

    @Override
    public boolean login(LoginRequestModel loginRequestModel) {
        return authDsGateway.existsByUsernameAndPassword(
                loginRequestModel.getUsername(),
                loginRequestModel.getPassword()
        );
    }
}