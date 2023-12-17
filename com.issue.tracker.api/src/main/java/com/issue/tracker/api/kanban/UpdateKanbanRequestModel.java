package com.issue.tracker.api.kanban;

import com.issue.tracker.api.persistence.kanban.KanbanDsResponseModel;

import java.util.List;

public class UpdateKanbanRequestModel extends KanbanDsResponseModel {
    public UpdateKanbanRequestModel() {
    }

    public UpdateKanbanRequestModel(Long id, String title, String description, Long ownerId, List<Long> admins, List<Long> participants) {
        super(id, title, description, ownerId, admins, participants);
    }
}
