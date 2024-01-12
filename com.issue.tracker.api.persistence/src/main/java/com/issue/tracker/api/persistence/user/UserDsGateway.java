package com.issue.tracker.api.persistence.user;

import com.issue.tracker.api.persistence.auth.UserDsResponseModel;
import jakarta.ejb.Local;
import jakarta.ejb.Remote;

import java.util.List;

@Remote
public interface UserDsGateway {
    List<UserDsResponseModel> findAllOtherUsers(Long currentUserId);
}
