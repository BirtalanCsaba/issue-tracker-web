package com.issue.tracker.api.persistence.kanban;

import jakarta.ejb.Remote;

import java.util.List;

@Remote
public interface KanbanDsGateway {
    KanbanDsResponseModel create(CreateKanbanDsRequestModel kanban);

    KanbanDsResponseModel findById(Long id);

    KanbanDsResponseModel findByTitle(String title);

    void update(UpdateKanbanDsRequestModel kanban);

    List<EnrolledKanbanDsResponseModel> findAllByUserId(Long userId);

    boolean isOwner(Long userId, Long kanbanId);

    boolean isAdmin(Long userId, Long kanbanId);

    boolean isParticipant(Long userId, Long kanbanId);

    void removeById(Long id);
}
