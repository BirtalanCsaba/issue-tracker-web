package com.issue.tracker.api.kanban;

import com.issue.tracker.api.ApiException;
import com.issue.tracker.api.auth.UserNotAuthorizedException;
import com.issue.tracker.api.persistence.kanban.*;
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
    public void update(UpdateKanbanRequestModel kanban, Long userId) {
        if (!kanbanDsGateway.isAdmin(userId, kanban.getId())) {
            throw new UserNotAuthorizedException("User with id: " + userId + " is not the owner of the kanban with id: " + kanban.getId());
        }
        kanbanDsGateway.update(new UpdateKanbanDsRequestModel(
                kanban.getId(),
                kanban.getTitle(),
                kanban.getDescription()
        ));
    }

    @Override
    public List<EnrolledKanbanResponseModel> findAllEnrolledKanbansForUser(Long userId) {
        List<EnrolledKanbanDsResponseModel> kanbans = kanbanDsGateway.findAllByUserId(userId);
        return kanbans.stream()
                .map(k -> new EnrolledKanbanResponseModel(
                        k.getId(),
                        k.getTitle(),
                        k.getDescription(),
                        k.getAdmins(),
                        k.getParticipants(),
                        k.getRole()
                )).toList();
    }

    @Override
    public void removeKanbanById(Long userId, Long kanbanId) {
        if (kanbanId == null) {
            throw new ApiException("Kanban ID is null");
        }
        if (!kanbanDsGateway.isOwner(userId, kanbanId)) {
            throw new UserNotAuthorizedException("User with id: " + userId + " is not the owner of the kanban with id: " + kanbanId);
        }
        kanbanDsGateway.removeById(kanbanId);
    }
}
