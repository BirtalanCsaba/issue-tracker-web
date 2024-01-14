package com.issue.tracker.api.user;

import com.issue.tracker.api.auth.UserResponseModel;
import com.issue.tracker.api.persistence.user.UserDsGateway;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;

import java.io.Serializable;
import java.util.List;

@Stateless
public class UserManagerInteractor implements UserManagerInput, Serializable {

    @EJB
    private UserDsGateway userDsGateway;

    @Override
    public List<UserResponseModel> findAllOtherUsers(Long currentUserId) {
        return userDsGateway.findAllOtherUsers(currentUserId).stream()
                .map(u -> new UserResponseModel(
                        u.getId(),
                        u.getFirstName(),
                        u.getLastName(),
                        u.getUsername(),
                        u.getEmail(),
                        u.getRole()
                )).toList();
    }
}
