package com.issue.tracker.api.auth;

import com.issue.tracker.api.persistence.auth.AuthDsGateway;
import com.issue.tracker.api.persistence.auth.SaveUserRequestModel;
import com.issue.tracker.api.persistence.auth.UserDsResponseModel;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;

import java.io.Serializable;

@Stateless
public class AuthInteractor implements AuthInput, Serializable {

    @EJB
    private AuthDsGateway authDsGateway;

    @Override
    public UserResponseModel register(RegisterRequestModel registerRequestModel) {
        if (authDsGateway.findByUsername(registerRequestModel.getUsername()) != null) {
            throw new UserAlreadyExistException("User with the same username already exists");
        }

        SaveUserRequestModel saveUserRequestModel = new SaveUserRequestModel(
                registerRequestModel.getFirstName(),
                registerRequestModel.getLastName(),
                registerRequestModel.getUsername(),
                registerRequestModel.getPassword(),
                registerRequestModel.getEmail()
        );

        UserDsResponseModel createdUser = authDsGateway.save(saveUserRequestModel);

        if (createdUser == null) {
            throw new UserCreationFailedException("User creation failed");
        }

        return new UserResponseModel(
                createdUser.getId(),
                createdUser.getFirstName(),
                createdUser.getLastName(),
                createdUser.getUsername(),
                createdUser.getEmail()
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