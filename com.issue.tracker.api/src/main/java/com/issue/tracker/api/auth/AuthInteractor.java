package com.issue.tracker.api.auth;

import com.issue.tracker.api.persistence.auth.AuthDsGateway;
import com.issue.tracker.api.persistence.auth.SaveUserRequestModel;
import com.issue.tracker.api.persistence.auth.UserDsCompleteResponseModel;
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

        UserDsCompleteResponseModel createdUser = authDsGateway.save(saveUserRequestModel);

        if (createdUser == null) {
            throw new UserCreationFailedException("User creation failed");
        }

        return new UserResponseModel(
                createdUser.getId(),
                createdUser.getFirstName(),
                createdUser.getLastName(),
                createdUser.getUsername(),
                createdUser.getEmail(),
                createdUser.getRole()
        );
    }

    @Override
    public boolean login(LoginRequestModel loginRequestModel) {
        return authDsGateway.existsByUsernameAndPassword(
                loginRequestModel.getUsername(),
                loginRequestModel.getPassword()
        );
    }

    @Override
    public UserResponseModel findByUsername(String username) {
        UserDsCompleteResponseModel theUser = authDsGateway.findByUsername(username);
        return new UserResponseModel(
                theUser.getId(),
                theUser.getFirstName(),
                theUser.getLastName(),
                theUser.getUsername(),
                theUser.getEmail(),
                theUser.getRole()
        );
    }
}