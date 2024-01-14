package com.issue.tracker.infra.persistence.user;

import com.issue.tracker.api.persistence.auth.UserDsResponseModel;
import com.issue.tracker.api.persistence.user.UserDsGateway;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;

import java.util.List;

@Stateless
public class UserDsGatewayImpl implements UserDsGateway {
    @EJB
    private UserRepository userRepository;

    @Override
    public List<UserDsResponseModel> findAllOtherUsers(Long currentUserId) {
        return userRepository.findAllOtherUsers(currentUserId).stream().map(
                u -> new UserDsResponseModel(
                        u.getId(),
                        u.getFirstName(),
                        u.getLastName(),
                        u.getUsername(),
                        u.getEmail(),
                        "NONE"
                )
        ).toList();
    }
}
