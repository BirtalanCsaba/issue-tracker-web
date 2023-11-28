package com.issue.tracker.infra.persistence.user;

import com.issue.tracker.api.persistence.auth.AuthDsGateway;
import com.issue.tracker.api.persistence.auth.SaveUserRequestModel;
import com.issue.tracker.api.persistence.auth.UserDsResponseModel;
import com.issue.tracker.infra.security.BCryptManager;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;

@Stateless
public class AuthDsGatewayImpl implements AuthDsGateway {

    @PersistenceContext(unitName = "jpa")
    private EntityManager entityManager;

    @Override
    public boolean existsByUsernameAndPassword(String username, String password) {
        String encryptedPassword = BCryptManager.encrypt(password);

        String queryString = "select count(u) > 0 from UserEntity u where username=:username and password=:password";

        Query query = entityManager.createQuery(queryString);
        query.setParameter("username", username);
        query.setParameter("password", encryptedPassword);

        return (Boolean) query.getSingleResult();
    }

    @Override
    public Boolean existsByUsernameAndPasswordAndIsActivated(String username, String password) {
        String encryptedPassword = BCryptManager.encrypt(password);

        String queryString = "select count(u) > 0 from UserEntity u where username=:username and password=:password and activated=true";

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

        try {
            UserEntity result = (UserEntity) query.getSingleResult();
            return new UserDsResponseModel(
                    result.getId(),
                    result.getFirstName(),
                    result.getLastName(),
                    result.getUsername(),
                    result.getPassword(),
                    result.getEmailConfirmationToken(),
                    result.getEmail()
            );
        } catch (NoResultException ex) {
            return null;
        }
    }

    @Override
    public UserDsResponseModel save(SaveUserRequestModel saveUserRequestModel) {
        String encryptedPassword = BCryptManager.encrypt(saveUserRequestModel.getPassword());
        entityManager.persist(
                new UserEntity(
                        saveUserRequestModel.getUsername(),
                        encryptedPassword,
                        saveUserRequestModel.getFirstName(),
                        saveUserRequestModel.getLastName(),
                        saveUserRequestModel.getEmailConfirmationToken(),
                        saveUserRequestModel.getEmail()
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
                result.getFirstName(),
                result.getLastName(),
                result.getUsername(),
                result.getPassword(),
                result.getEmailConfirmationToken(),
                result.getEmail()
        ) : null;
    }

    @Override
    public UserDsResponseModel findByEmail(String email) {
        String queryString = "select u from UserEntity u where email=:email";

        Query query = entityManager.createQuery(queryString);
        query.setParameter("email", email);

        try {
            UserEntity result = (UserEntity) query.getSingleResult();
            return new UserDsResponseModel(
                    result.getId(),
                    result.getFirstName(),
                    result.getLastName(),
                    result.getUsername(),
                    result.getPassword(),
                    result.getEmailConfirmationToken(),
                    result.getEmail()
            );
        } catch (NoResultException ex) {
            return null;
        }
    }
}
