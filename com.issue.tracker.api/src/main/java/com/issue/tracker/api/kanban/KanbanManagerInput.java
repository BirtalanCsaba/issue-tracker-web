package com.issue.tracker.api.kanban;

import jakarta.ejb.Local;

import java.util.List;

@Local
public interface KanbanManagerInput {
    KanbanResponseModel create(CreateKanbanRequestModel kanban);

    void update(UpdateKanbanRequestModel kanban, Long userId);

    List<EnrolledKanbanResponseModel> findAllEnrolledKanbansForUser(Long userId);

    void removeKanbanById(Long userId, Long kanbanId);

    KanbanResponseModel findById(Long kanbanId);

    KanbanCompleteResponseModel findCompleteById(Long kanbanId);
}
