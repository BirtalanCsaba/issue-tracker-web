package com.issue.tracker.api.auth;

public class UserCreationFailedException extends RuntimeException {
    public UserCreationFailedException() {
    }

    public UserCreationFailedException(String message) {
        super(message);
    }

    public UserCreationFailedException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserCreationFailedException(Throwable cause) {
        super(cause);
    }
}
