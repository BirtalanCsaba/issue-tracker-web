package com.issue.tracker.api.kanban;

import jakarta.ejb.Local;

import java.util.List;

@Local
public interface KanbanManagerInput {
    KanbanResponseModel create(CreateKanbanRequestModel kanban);

    List<KanbanResponseModel> findAllEnrolledKanbansForUser(Long userId);

    void removeKanbanById(Long kanbanId);
}
