package com.issue.tracker.infra.web.auth;


import com.issue.tracker.api.logger.LogType;
import com.issue.tracker.api.logger.LoggerBuilder;
import com.issue.tracker.infra.security.JWTKeys;
import jakarta.ejb.EJB;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.SecurityContext;
import jakarta.ws.rs.ext.Provider;

import java.io.IOException;

@Provider
@Authenticated
public class SecurityRequestFilter implements ContainerRequestFilter {
    @EJB
    private LoggerBuilder loggerBuilder;

    @EJB
    private JWTKeys jwtKeys;

    @Override
    public void filter(ContainerRequestContext requestContext) {
        String authorizationHeader = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);
        if (authorizationHeader == null || authorizationHeader.isEmpty()) {
            throw new AuthorizationFailedException("Authorization token not provided");
        }
        String token = authorizationHeader.substring("Bearer ".length());
        if (!jwtKeys.isTokenValid(token)) {
            throw new AuthorizationFailedException("Invalid token");
        }
        UserPrincipal userPrincipal = new UserPrincipal(jwtKeys.getSubject(token));
        UserSecurityContext userSecurityContext = new UserSecurityContext(userPrincipal);
        requestContext.setSecurityContext(userSecurityContext);
    }
}
