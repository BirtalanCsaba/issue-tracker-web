package com.issue.tracker.api.kanban;

import com.issue.tracker.api.ApiException;
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
                response.getOwners(),
                response.getParticipants()
        );
    }

    @Override
    public List<KanbanResponseModel> findAllEnrolledKanbansForUser(Long userId) {
        List<KanbanDsResponseModel> kanbans = kanbanDsGateway.findAllByUserId(userId);
        return kanbans.stream()
                .map(k -> new KanbanResponseModel(
                        k.getId(),
                        k.getTitle(),
                        k.getDescription(),
                        k.getOwners(),
                        k.getParticipants()
                )).toList();
    }

    @Override
    public void removeKanbanById(Long kanbanId) {
        if (kanbanId == null) {
            throw new ApiException("Kanban ID is null");
        }
        if (kanbanDsGateway.findById(kanbanId) == null) {
            throw new ApiException("Kanban not found with id: " + kanbanId);
        }
        kanbanDsGateway.removeById(kanbanId);
    }
}
