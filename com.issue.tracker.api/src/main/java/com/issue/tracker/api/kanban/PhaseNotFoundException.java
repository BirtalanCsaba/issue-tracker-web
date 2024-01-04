package com.issue.tracker.api.kanban;

import com.issue.tracker.api.ApiException;

public class PhaseNotFoundException extends ApiException {
    public PhaseNotFoundException() {
    }

    public PhaseNotFoundException(String message) {
        super(message);
    }

    public PhaseNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public PhaseNotFoundException(Throwable cause) {
        super(cause);
    }
}
