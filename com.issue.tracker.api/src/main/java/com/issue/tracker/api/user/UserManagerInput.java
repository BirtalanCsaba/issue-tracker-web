package com.issue.tracker.api.user;

import com.issue.tracker.api.auth.UserResponseModel;
import jakarta.ejb.Local;

import java.util.List;

@Local
public interface UserManagerInput {
    List<UserResponseModel> findAllOtherUsers(Long currentUserId);
}
