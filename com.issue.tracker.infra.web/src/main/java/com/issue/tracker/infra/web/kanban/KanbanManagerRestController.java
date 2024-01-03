package com.issue.tracker.infra.web.kanban;

import com.issue.tracker.api.auth.AuthInput;
import com.issue.tracker.api.auth.UserNotAuthorizedException;
import com.issue.tracker.api.kanban.*;
import com.issue.tracker.api.logger.LogType;
import com.issue.tracker.api.logger.LoggerBuilder;
import com.issue.tracker.infra.web.auth.Authenticated;
import com.issue.tracker.infra.web.common.GenericErrorResponse;
import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;

import java.util.Arrays;

@Path("/kanban")
@Authenticated
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class KanbanManagerRestController {
    @EJB
    private KanbanManagerInput kanbanManager;

    @EJB
    private AuthInput authManager;

    @EJB
    private LoggerBuilder loggerBuilder;

    @POST
    public Response createKanban(
            @Context SecurityContext securityContext,
            CreateKanbanRestRequestModel kanban) {
        try {
            var currentAuthenticatedUser = authManager.findByUsername(securityContext.getUserPrincipal().getName());
            return Response.ok().entity(kanbanManager.create(
                    new CreateKanbanRequestModel(
                            kanban.getTitle(),
                            kanban.getDescription(),
                            currentAuthenticatedUser.getId(),
                            kanban.getAdmins(),
                            kanban.getParticipants()
                    )
            )).build();
        } catch (RuntimeException ex) {
            loggerBuilder.create(
                            getClass(),
                            LogType.ERROR,
                            ex.getMessage()
                    )
                    .build()
                    .print();
            GenericErrorResponse errorResponse = new GenericErrorResponse("Kanban creation failed");
            return Response.status(Response.Status.BAD_REQUEST).entity(errorResponse).build();
        }
    }

    @PUT
    public Response update(@Context SecurityContext securityContext,
                           UpdateKanbanRequestModel kanban) {
        try {
            var currentAuthenticatedUser = authManager.findByUsername(securityContext.getUserPrincipal().getName());
            kanbanManager.update(kanban, currentAuthenticatedUser.getId());
            return Response.ok().build();
        } catch (RuntimeException ex) {
            loggerBuilder.create(
                            getClass(),
                            LogType.ERROR,
                            ex.getMessage()
                    )
                    .build()
                    .print();
            GenericErrorResponse errorResponse = new GenericErrorResponse("Cannot get kanbans for user");
            return Response.status(Response.Status.BAD_REQUEST).entity(errorResponse).build();
        }
    }

    @GET
    @Path("/user/{userId}")
    public Response findAllForUser(@PathParam("userId") Long userId) {
        try {
            return Response.ok(kanbanManager.findAllEnrolledKanbansForUser(userId)).build();
        } catch (RuntimeException ex) {
            loggerBuilder.create(
                            getClass(),
                            LogType.ERROR,
                            ex.getMessage()
                    )
                    .build()
                    .print();
            GenericErrorResponse errorResponse = new GenericErrorResponse("Cannot get kanbans for user");
            return Response.status(Response.Status.BAD_REQUEST).entity(errorResponse).build();
        }
    }

    @GET
    @Path("/{kanbanId}")
    public Response findById(@PathParam("kanbanId") Long kanbanId,
                             @Context SecurityContext securityContext) {
        try {
            return Response.ok(kanbanManager.findById(kanbanId)).build();
        } catch (UserNotAuthorizedException ex) {
            loggerBuilder.create(
                            getClass(),
                            LogType.WARNING,
                            "User not authorized"
                    )
                    .withReason("User should be the owner of the Kanban to perform this action")
                    .build()
                    .print();
            GenericErrorResponse errorResponse = new GenericErrorResponse("User not authorized to perform the action");
            return Response.status(Response.Status.UNAUTHORIZED).entity(errorResponse).build();
        } catch (RuntimeException ex) {
            loggerBuilder.create(
                            getClass(),
                            LogType.ERROR,
                            ex.getMessage()
                    )
                    .withStackTrace(Arrays.toString(ex.getStackTrace()))
                    .build()
                    .print();
            GenericErrorResponse errorResponse = new GenericErrorResponse("Something went wrong");
            return Response.status(Response.Status.BAD_REQUEST).entity(errorResponse).build();
        }
    }

    @GET
    @Path("/complete/{kanbanId}")
    public Response findCompleteById(@PathParam("kanbanId") Long kanbanId,
                             @Context SecurityContext securityContext) {
        try {
            return Response.ok(kanbanManager.findCompleteById(kanbanId)).build();
        } catch (UserNotAuthorizedException ex) {
            loggerBuilder.create(
                            getClass(),
                            LogType.WARNING,
                            "User not authorized"
                    )
                    .withReason("User should be the owner of the Kanban to perform this action")
                    .build()
                    .print();
            GenericErrorResponse errorResponse = new GenericErrorResponse("User not authorized to perform the action");
            return Response.status(Response.Status.UNAUTHORIZED).entity(errorResponse).build();
        } catch (RuntimeException ex) {
            loggerBuilder.create(
                            getClass(),
                            LogType.ERROR,
                            ex.getMessage()
                    )
                    .withStackTrace(Arrays.toString(ex.getStackTrace()))
                    .build()
                    .print();
            GenericErrorResponse errorResponse = new GenericErrorResponse("Something went wrong");
            return Response.status(Response.Status.BAD_REQUEST).entity(errorResponse).build();
        }
    }

    @DELETE
    @Path("/{kanbanId}")
    public Response delete(
            @PathParam("kanbanId") Long kanbanId,
            @Context SecurityContext securityContext) {
        try {
            var currentAuthenticatedUser = authManager.findByUsername(securityContext.getUserPrincipal().getName());
            kanbanManager.removeKanbanById(currentAuthenticatedUser.getId(), kanbanId);
            return Response.ok().build();
        } catch (UserNotAuthorizedException ex) {
            loggerBuilder.create(
                            getClass(),
                            LogType.WARNING,
                            "User not authorized"
                    )
                    .withReason("User should be the owner of the Kanban to perform this action")
                    .build()
                    .print();
            GenericErrorResponse errorResponse = new GenericErrorResponse("User not authorized to perform the action");
            return Response.status(Response.Status.UNAUTHORIZED).entity(errorResponse).build();
        } catch (RuntimeException ex) {
            loggerBuilder.create(
                            getClass(),
                            LogType.ERROR,
                            ex.getMessage()
                    )
                    .withStackTrace(Arrays.toString(ex.getStackTrace()))
                    .build()
                    .print();
            GenericErrorResponse errorResponse = new GenericErrorResponse("Something went wrong");
            return Response.status(Response.Status.BAD_REQUEST).entity(errorResponse).build();
        }
    }

    @POST
    @Path("/phase/{kanbanId}")
    public Response addPhase(@PathParam("kanbanId") Long kanbanId,
                             @Context SecurityContext securityContext,
                             PhaseRequestModel phase) {
        try {
            var currentAuthenticatedUser = authManager.findByUsername(securityContext.getUserPrincipal().getName());
            PhaseResponseModel createdPhase = kanbanManager.addPhase(
                    currentAuthenticatedUser.getId(),
                    kanbanId,
                    phase.getTitle()
            );
            return Response.ok(createdPhase).build();
        } catch (UserNotAuthorizedException ex) {
            loggerBuilder.create(
                            getClass(),
                            LogType.WARNING,
                            "User not authorized"
                    )
                    .withReason("User should be the owner of the Kanban to perform this action")
                    .build()
                    .print();
            GenericErrorResponse errorResponse = new GenericErrorResponse("User not authorized to perform the action");
            return Response.status(Response.Status.UNAUTHORIZED).entity(errorResponse).build();
        } catch (RuntimeException ex) {
            loggerBuilder.create(
                            getClass(),
                            LogType.ERROR,
                            ex.getMessage()
                    )
                    .withStackTrace(Arrays.toString(ex.getStackTrace()))
                    .build()
                    .print();
            GenericErrorResponse errorResponse = new GenericErrorResponse("Something went wrong");
            return Response.status(Response.Status.BAD_REQUEST).entity(errorResponse).build();
        }
    }
}
