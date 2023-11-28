package com.issue.tracker.api.auth;

import jakarta.ejb.Remote;

@Remote
public interface AuthEmailSender {
    void sendUserRegistrationEmailConfirmation(String to, String token);
}
