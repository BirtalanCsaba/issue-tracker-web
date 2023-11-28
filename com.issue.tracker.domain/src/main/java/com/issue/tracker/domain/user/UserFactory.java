package com.issue.tracker.domain.user;

public interface UserFactory {
    User createUser(String firstName, String lastName, String username, String password);
}