package com.issue.tracker.infra.web.auth;

import java.security.Principal;

public class UserPrincipal implements Principal {
    private final String username;

    UserPrincipal(String username) {
        this.username = username;
    }

    @Override
    public String getName() {
        return username;
    }
}
