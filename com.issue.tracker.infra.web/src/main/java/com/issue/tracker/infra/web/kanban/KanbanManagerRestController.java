package com.issue.tracker.infra.web.kanban;

import com.issue.tracker.api.auth.AuthInput;
import com.issue.tracker.api.kanban.CreateKanbanRequestModel;
import com.issue.tracker.api.kanban.KanbanManagerInput;
import com.issue.tracker.api.logger.LogType;
import com.issue.tracker.api.logger.LoggerBuilder;
import com.issue.tracker.infra.web.auth.Authenticated;
import jakarta.ejb.EJB;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;

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
                            "Kanban creation failed"
                    )
                    .build()
                    .print();
            return Response.status(Response.Status.NOT_ACCEPTABLE).build();
        }
    }
}
