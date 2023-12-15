package com.issue.tracker.infra.web.auth;

import com.issue.tracker.api.auth.AuthInput;
import com.issue.tracker.api.auth.LoginRequestModel;
import com.issue.tracker.api.auth.RegisterRequestModel;
import com.issue.tracker.api.logger.LogType;
import com.issue.tracker.api.logger.LoggerBuilder;
import com.issue.tracker.infra.security.JWTKeys;
import jakarta.ejb.EJB;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.HashMap;
import java.util.Map;

@Path("/auth")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class AuthenticationRestController {
    @EJB
    private AuthInput authInteractor;

    @EJB
    private LoggerBuilder loggerBuilder;

    @EJB
    private JWTKeys jwtKeys;

    @POST
    @Path("/login")
    public Response login(LoginRequestModel loginRequestModel) {
        try {
            boolean authenticated = authInteractor.login(loginRequestModel);
            if (!authenticated) {
                return Response.status(Response.Status.FORBIDDEN).build();
            }
            // generate access and refresh tokens
            String accessToken = jwtKeys.createAccessToken(loginRequestModel.getUsername());
            String refreshToken = jwtKeys.createRefreshToken(loginRequestModel.getUsername());
            Map<String, String> tokens = new HashMap<>();
            tokens.put("accessToken", accessToken);
            tokens.put("refreshToken", refreshToken);
            return Response.ok(tokens).build();
        } catch (RuntimeException ex) {
            loggerBuilder.create(getClass(), LogType.ERROR, ex.getMessage())
                    .build()
                    .print();
            return Response.serverError().build();
        }
    }

    @POST
    @Path("/register")
    public Response register(RegisterRequestModel registrationData) {
        try {
            return Response.ok(authInteractor.register(registrationData)).build();
        } catch (RuntimeException ex) {
            loggerBuilder.create(getClass(), LogType.ERROR, ex.getMessage())
                    .build()
                    .print();
            return Response.status(Response.Status.NOT_ACCEPTABLE).build();
        }
    }
}
