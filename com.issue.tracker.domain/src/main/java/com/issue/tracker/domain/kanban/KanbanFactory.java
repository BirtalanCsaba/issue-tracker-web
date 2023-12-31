package com.issue.tracker.domain.kanban;

import java.util.List;

public interface KanbanFactory {
    Kanban create(
            String title,
            String description,
            Long ownerId,
            List<Long> admins,
            List<Long> participants
    );

    Kanban create(
            String title,
            String description
    );
}
