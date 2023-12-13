package com.issue.tracker.api.kanban;

import com.issue.tracker.api.persistence.kanban.CreateKanbanDsRequestModel;
import com.issue.tracker.api.persistence.kanban.KanbanDsGateway;
import com.issue.tracker.api.persistence.kanban.KanbanDsResponseModel;
import com.issue.tracker.domain.kanban.Kanban;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;

import java.util.List;

@Stateless
public class KanbanInteractor implements KanbanManagerInput {
    @EJB
    private KanbanDsGateway kanbanDsGateway;

    @Override
    public KanbanResponseModel create(CreateKanbanRequestModel kanban) {
        Kanban newKanban = new Kanban(
                kanban.getTitle(),
                kanban.getDescription(),
                kanban.getOwnerId(),
                kanban.getParticipants()
        );
        KanbanDsResponseModel response = kanbanDsGateway.create(new CreateKanbanDsRequestModel(
                newKanban.getTitle(),
                newKanban.getDescription(),
                newKanban.getOwner(),
                newKanban.getParticipants()
        ));
        return new KanbanResponseModel(
                response.getId(),
                response.getTitle(),
                response.getDescription(),
                response.getOwnerId(),
                response.getParticipants()
        );
    }

    @Override
    public List<KanbanResponseModel> findAllEnrolledKanbanForUser(Long userId) {
        return null;
    }
}
