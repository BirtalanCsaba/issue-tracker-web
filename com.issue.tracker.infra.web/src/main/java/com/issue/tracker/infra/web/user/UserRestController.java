package com.issue.tracker.infra.web.user;

import com.issue.tracker.api.auth.AuthInput;
import com.issue.tracker.api.logger.LogType;
import com.issue.tracker.api.logger.LoggerBuilder;
import com.issue.tracker.api.user.UserManagerInput;
import com.issue.tracker.infra.web.auth.Authenticated;
import com.issue.tracker.infra.web.common.GenericErrorResponse;
import jakarta.ejb.EJB;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;

@Path("/user")
@Authenticated
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserRestController {
    @EJB
    private LoggerBuilder loggerBuilder;

    @EJB
    private UserManagerInput userService;

    @EJB
    private AuthInput authManager;

    @GET
    @Path("/other")
    public Response findAllOtherUsers(@Context SecurityContext securityContext) {
        try {
            var currentAuthenticatedUser = authManager.findByUsername(securityContext.getUserPrincipal().getName());
            var response = userService.findAllOtherUsers(currentAuthenticatedUser.getId());
            return Response.ok(response).build();
        } catch (RuntimeException ex) {
            loggerBuilder.create(
                            getClass(),
                            LogType.ERROR,
                            ex.getMessage()
                    )
                    .build()
                    .print();
            GenericErrorResponse errorResponse = new GenericErrorResponse("Cannot get users");
            return Response.status(Response.Status.BAD_REQUEST).entity(errorResponse).build();
        }
    }
}
