package com.issue.tracker.api.persistence.kanban;

import com.issue.tracker.api.persistence.ApiPersistenceException;

public class IssueNotFoundException extends ApiPersistenceException {
    public IssueNotFoundException() {
    }

    public IssueNotFoundException(String message) {
        super(message);
    }

    public IssueNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public IssueNotFoundException(Throwable cause) {
        super(cause);
    }

    public IssueNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
