package com.issue.tracker.infra.persistence.user;

import jakarta.ejb.Local;

import java.util.List;

@Local
public interface UserRepository extends BaseRepository<UserEntity, Long> {
    UserEntity findByUsernameAndPassword(String username, String password);

    UserEntity findByUsername(String username);

    UserEntity findByEmail(String email);

    List<UserEntity> findAllUsersWithIds(List<Long> userIds);
}
