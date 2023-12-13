package com.issue.tracker.domain.kanban;

import java.util.List;

public interface KanbanFactory {
    Kanban create(String title, String description, Long ownerId, List<Long> participants);
}
