package com.issue.tracker.domain.user;

import com.issue.tracker.domain.ValidationException;

public class BaseUserFactory implements UserFactory {

    @Override
    public User createUser(String firstName, String lastName, String username, String password) {
        username = sanitizeUsername(username);
        password = sanitizePassword(password);
        firstName = sanitizeName(firstName);
        lastName = sanitizeName(lastName);
        validateUsername(username);
        validatePassword(password);
        validateName(firstName);
        validateName(lastName);

        return new User(username, password);
    }

    private void validateUsername(String username) {
        // should not contain spaces
        if (username.matches("^\\S+$")) {
            throw new ValidationException("Username should not be empty or contain spaces");
        }
    }

    private void validatePassword(String password) {
        // should not contain spaces
        if (password.matches("^\\S+$")) {
            throw new ValidationException("Password should not be empty or contain spaces");
        }
    }

    private void validateName(String name) {
        if (name.isEmpty()) {
            throw new ValidationException("Name is empty");
        }
    }

    private String sanitizeName(String name) {
        return name.trim();
    }

    private String sanitizeUsername(String username) {
        return username.trim();
    }

    private String sanitizePassword(String password) {
        return password.trim();
    }
}