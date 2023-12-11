package com.issue.tracker.api.persistence;

public class ApiPersistenceException extends RuntimeException {
    public ApiPersistenceException() {
    }

    public ApiPersistenceException(String message) {
        super(message);
    }

    public ApiPersistenceException(String message, Throwable cause) {
        super(message, cause);
    }

    public ApiPersistenceException(Throwable cause) {
        super(cause);
    }

    public ApiPersistenceException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
