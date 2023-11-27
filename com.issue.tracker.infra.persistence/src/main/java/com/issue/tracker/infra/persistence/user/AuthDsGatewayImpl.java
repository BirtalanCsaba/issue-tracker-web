package com.issue.tracker.infra.persistence.user;

import com.issue.tracker.api.persistence.auth.AuthDsGateway;
import com.issue.tracker.api.persistence.auth.SaveUserRequestModel;
import com.issue.tracker.api.persistence.auth.UserDsResponseModel;
import com.issue.tracker.infra.security.BCryptManager;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;

@Stateless
public class AuthDsGatewayImpl implements AuthDsGateway {

    @PersistenceContext(unitName = "jpa")
    private EntityManager entityManager;

    @Override
    public boolean existsByUsernameAndPassword(String username, String password) {
        String encryptedPassword = BCryptManager.encrypt(password);

        String queryString = "select exists(u) from UserEntity u where username=:username and password=:password";

        Query query = entityManager.createQuery(queryString);
        query.setParameter("username", username);
        query.setParameter("password", encryptedPassword);

        return (Boolean) query.getSingleResult();
    }

    @Override
    public UserDsResponseModel findByUsername(String username) {
        String queryString = "select u from UserEntity u where username=:username";

        Query query = entityManager.createQuery(queryString);
        query.setParameter("username", username);

        UserEntity result = (UserEntity) query.getSingleResult();
        return result != null ? new UserDsResponseModel(
                result.getId(),
                result.getUsername(),
                result.getPassword(),
                result.getFirstName(),
                result.getLastName()
        ) : null;
    }

    @Override
    public UserDsResponseModel save(SaveUserRequestModel saveUserRequestModel) {
        String encryptedPassword = BCryptManager.encrypt(saveUserRequestModel.getPassword());
        entityManager.persist(
                new UserEntity(
                        saveUserRequestModel.getUsername(),
                        encryptedPassword,
                        saveUserRequestModel.getFirstName(),
                        saveUserRequestModel.getLastName()
                )
        );
        return findByUsername(saveUserRequestModel.getUsername());
    }

    @Override
    public UserDsResponseModel findById(Long id) {
        String queryString = "select u from UserEntity u where id=:id";
        Query query = entityManager.createQuery(queryString);
        query.setParameter("id", id);

        UserEntity result = (UserEntity) query.getSingleResult();
        return result != null ? new UserDsResponseModel(
                result.getId(),
                result.getUsername(),
                result.getPassword(),
                result.getFirstName(),
                result.getLastName()
        ) : null;
    }
}
