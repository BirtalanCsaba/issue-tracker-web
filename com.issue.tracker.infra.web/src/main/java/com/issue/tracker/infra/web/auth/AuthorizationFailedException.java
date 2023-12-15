package com.issue.tracker.infra.web.auth;

import com.issue.tracker.infra.web.common.InfraWebException;

public class AuthorizationFailedException extends InfraWebException {
    public AuthorizationFailedException() {
    }

    public AuthorizationFailedException(String message) {
        super(message);
    }

    public AuthorizationFailedException(String message, Throwable cause) {
        super(message, cause);
    }

    public AuthorizationFailedException(Throwable cause) {
        super(cause);
    }
}
