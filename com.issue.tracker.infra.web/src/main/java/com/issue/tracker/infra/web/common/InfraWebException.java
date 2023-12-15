package com.issue.tracker.infra.web.common;

public class InfraWebException extends RuntimeException {
    public InfraWebException() {
    }

    public InfraWebException(String message) {
        super(message);
    }

    public InfraWebException(String message, Throwable cause) {
        super(message, cause);
    }

    public InfraWebException(Throwable cause) {
        super(cause);
    }
}
