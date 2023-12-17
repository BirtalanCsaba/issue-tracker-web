package com.issue.tracker.infra.web.common;

import java.io.Serializable;

public class GenericErrorResponse implements Serializable {
    String exception;

    public GenericErrorResponse() {

    }

    public GenericErrorResponse(String exception) {
        this.exception = exception;
    }

    public String getException() {
        return exception;
    }

    public void setException(String exception) {
        this.exception = exception;
    }
}
