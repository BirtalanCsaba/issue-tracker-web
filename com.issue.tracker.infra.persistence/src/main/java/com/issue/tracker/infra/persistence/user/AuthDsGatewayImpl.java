package com.issue.tracker.infra.persistence.user;

import com.issue.tracker.api.persistence.auth.AuthDsGateway;
import com.issue.tracker.api.persistence.auth.SaveUserRequestModel;
import com.issue.tracker.api.persistence.auth.UserDsResponseModel;
import com.issue.tracker.infra.security.BCryptManager;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;

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
    public UserDsResponseModel findByUsername(String username) {
        UserEntity result = userRepository.findByUsername(username);
        return result != null ? new UserDsResponseModel(
                result.getId(),
                result.getFirstName(),
                result.getLastName(),
                result.getUsername(),
                result.getPassword(),
                result.getEmail()
        ) : null;
    }

    @Override
    public UserDsResponseModel save(SaveUserRequestModel saveUserRequestModel) {
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
        return new UserDsResponseModel(
                createdUser.getId(),
                createdUser.getFirstName(),
                createdUser.getLastName(),
                createdUser.getUsername(),
                createdUser.getPassword(),
                createdUser.getEmail()
        );
    }

    @Override
    public UserDsResponseModel findById(Long id) {
        UserEntity result = userRepository.findById(id);
        return result != null ? new UserDsResponseModel(
                result.getId(),
                result.getFirstName(),
                result.getLastName(),
                result.getUsername(),
                result.getPassword(),
                result.getEmail()
        ) : null;
    }

    @Override
    public UserDsResponseModel findByEmail(String email) {
        UserEntity result = userRepository.findByEmail(email);
        return result != null ? new UserDsResponseModel(
                result.getId(),
                result.getFirstName(),
                result.getLastName(),
                result.getUsername(),
                result.getPassword(),
                result.getEmail()
        ) : null;
    }
}
