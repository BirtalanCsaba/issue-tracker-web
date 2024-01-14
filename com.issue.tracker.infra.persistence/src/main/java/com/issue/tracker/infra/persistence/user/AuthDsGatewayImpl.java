package com.issue.tracker.infra.persistence.user;

import com.issue.tracker.api.persistence.auth.AuthDsGateway;
import com.issue.tracker.api.persistence.auth.SaveUserRequestModel;
import com.issue.tracker.api.persistence.auth.UserDsCompleteResponseModel;
import com.issue.tracker.infra.security.BCryptManager;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;

@Stateless
public class AuthDsGatewayImpl implements AuthDsGateway {
    @EJB
    private UserRepository userRepository;

    @Override
    public boolean existsByUsernameAndPassword(String username, String password) {
        UserEntity user = userRepository.findByUsername(username);
        if (user == null) {
            return false;
        }
        return BCryptManager.matches(password, user.getPassword());
    }

    @Override
    public UserDsCompleteResponseModel findByUsername(String username) {
        UserEntity result = userRepository.findByUsername(username);
        return result != null ? new UserDsCompleteResponseModel(
                result.getId(),
                result.getFirstName(),
                result.getLastName(),
                result.getUsername(),
                result.getPassword(),
                result.getEmail()
        ) : null;
    }

    @Override
    public UserDsCompleteResponseModel save(SaveUserRequestModel saveUserRequestModel) {
        String encryptedPassword = BCryptManager.encrypt(saveUserRequestModel.getPassword());
        UserEntity createdUser = userRepository.save(
                new UserEntity(
                        saveUserRequestModel.getUsername(),
                        encryptedPassword,
                        saveUserRequestModel.getFirstName(),
                        saveUserRequestModel.getLastName(),
                        saveUserRequestModel.getEmail()
                )
        );
        return new UserDsCompleteResponseModel(
                createdUser.getId(),
                createdUser.getFirstName(),
                createdUser.getLastName(),
                createdUser.getUsername(),
                createdUser.getPassword(),
                createdUser.getEmail()
        );
    }

    @Override
    public UserDsCompleteResponseModel findById(Long id) {
        UserEntity result = userRepository.findById(id);
        return result != null ? new UserDsCompleteResponseModel(
                result.getId(),
                result.getFirstName(),
                result.getLastName(),
                result.getUsername(),
                result.getPassword(),
                result.getEmail()
        ) : null;
    }

    @Override
    public UserDsCompleteResponseModel findByEmail(String email) {
        UserEntity result = userRepository.findByEmail(email);
        return result != null ? new UserDsCompleteResponseModel(
                result.getId(),
                result.getFirstName(),
                result.getLastName(),
                result.getUsername(),
                result.getPassword(),
                result.getEmail()
        ) : null;
    }
}
