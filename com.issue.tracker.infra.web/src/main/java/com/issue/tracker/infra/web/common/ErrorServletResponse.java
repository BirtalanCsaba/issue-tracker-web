package com.issue.tracker.infra.web.common;

import java.io.Serializable;

public class ErrorServletResponse implements Serializable {
    String exception;

    public ErrorServletResponse() {

    }

    public ErrorServletResponse(String exception) {
        this.exception = exception;
    }

    public String getException() {
        return exception;
    }

    public void setException(String exception) {
        this.exception = exception;
    }
}
