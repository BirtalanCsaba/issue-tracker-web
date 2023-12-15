package com.issue.tracker.api.persistence.kanban;

import com.issue.tracker.api.persistence.ApiPersistenceException;

public class SomeUsersNotFoundException extends ApiPersistenceException {
    public SomeUsersNotFoundException() {
    }

    public SomeUsersNotFoundException(String message) {
        super(message);
    }

    public SomeUsersNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public SomeUsersNotFoundException(Throwable cause) {
        super(cause);
    }

    public SomeUsersNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
