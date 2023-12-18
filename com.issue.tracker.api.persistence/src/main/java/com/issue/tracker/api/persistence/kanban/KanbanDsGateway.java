package com.issue.tracker.api.persistence.kanban;

import com.issue.tracker.api.persistence.auth.UserDsResponseModel;
import jakarta.ejb.Remote;

import java.util.List;

@Remote
public interface KanbanDsGateway {
    KanbanDsResponseModel create(CreateKanbanDsRequestModel kanban);

    KanbanDsResponseModel findById(Long id);

    KanbanDsCompleteResponseModel findCompleteById(Long id);

    KanbanDsResponseModel findByTitle(String title);

    void update(UpdateKanbanDsRequestModel kanban);

    List<EnrolledKanbanDsResponseModel> findAllByUserId(Long userId);

    boolean isOwner(Long userId, Long kanbanId);

    boolean isAdmin(Long userId, Long kanbanId);

    boolean isParticipant(Long userId, Long kanbanId);

    void removeById(Long id);
}
