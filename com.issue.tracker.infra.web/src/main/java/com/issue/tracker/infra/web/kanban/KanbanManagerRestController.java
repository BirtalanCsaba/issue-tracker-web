package com.issue.tracker.infra.web.kanban;

import com.issue.tracker.api.kanban.CreateKanbanRequestModel;
import com.issue.tracker.api.kanban.KanbanManagerInput;
import com.issue.tracker.api.logger.LogType;
import com.issue.tracker.api.logger.LoggerBuilder;
import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/kanban")
public class KanbanManagerRestController {
    @EJB
    private KanbanManagerInput kanbanManager;

    @EJB
    private LoggerBuilder loggerBuilder;

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createKanban(CreateKanbanRequestModel kanban) {
        try {
            return Response.ok().entity(kanbanManager.create(kanban)).build();
        } catch (RuntimeException ex) {
            loggerBuilder.create(getClass(), LogType.ERROR, "Kanban creation failed")
                    .build()
                    .print();
            return Response.status(Response.Status.NOT_ACCEPTABLE).build();
        }
    }
}
