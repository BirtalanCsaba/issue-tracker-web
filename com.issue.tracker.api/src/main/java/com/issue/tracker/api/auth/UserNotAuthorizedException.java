package com.issue.tracker.api.auth;

import com.issue.tracker.api.ApiException;
import jakarta.ejb.EJBException;

public class UserNotAuthorizedException extends EJBException {
    public UserNotAuthorizedException() {
    }

    public UserNotAuthorizedException(String message) {
        super(message);
    }
}
