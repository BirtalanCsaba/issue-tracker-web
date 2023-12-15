package com.issue.tracker.api.kanban;

import com.issue.tracker.api.ApiException;
import com.issue.tracker.api.persistence.kanban.CreateKanbanDsRequestModel;
import com.issue.tracker.api.persistence.kanban.KanbanDsGateway;
import com.issue.tracker.api.persistence.kanban.KanbanDsResponseModel;
import com.issue.tracker.domain.kanban.BaseKanbanFactory;
import com.issue.tracker.domain.kanban.Kanban;
import com.issue.tracker.domain.kanban.KanbanFactory;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;

import java.util.List;

@Stateless
public class KanbanInteractor implements KanbanManagerInput {
    @EJB
    private KanbanDsGateway kanbanDsGateway;

    private final KanbanFactory kanbanFactory = new BaseKanbanFactory();

    @Override
    public KanbanResponseModel create(CreateKanbanRequestModel kanban) {
        Kanban newKanban = kanbanFactory.create(
                kanban.getTitle(),
                kanban.getDescription(),
                kanban.getOwnerId(),
                kanban.getAdmins(),
                kanban.getParticipants()
        );
        KanbanDsResponseModel response = kanbanDsGateway.create(new CreateKanbanDsRequestModel(
                newKanban.getTitle(),
                newKanban.getDescription(),
                newKanban.getOwner(),
                kanban.getAdmins(),
                newKanban.getParticipants()
        ));
        return new KanbanResponseModel(
                response.getId(),
                response.getTitle(),
                response.getDescription(),
                response.getAdmins(),
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
                        k.getAdmins(),
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
