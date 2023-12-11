package com.issue.tracker.api.kanban;

import jakarta.ejb.Local;
import jakarta.ejb.Remote;

import java.util.List;

@Local
public interface KanbanManagerInput {
    KanbanResponseModel create(CreateKanbanRequestModel kanban);

    List<KanbanResponseModel> findAllEnrolledKanbanForUser(Long userId);
}
