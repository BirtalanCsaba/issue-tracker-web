package com.issue.tracker.api.kanban;

import jakarta.ejb.Stateless;

import java.util.List;

@Stateless
public class KanbanInteractor implements KanbanManagerInput {
    @Override
    public KanbanResponseModel create(CreateKanbanRequestModel kanban) {
        return null;
    }

    @Override
    public List<KanbanResponseModel> findAllEnrolledKanbanForUser(Long userId) {
        return null;
    }
}
