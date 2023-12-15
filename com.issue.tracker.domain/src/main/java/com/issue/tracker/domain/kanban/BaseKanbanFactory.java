package com.issue.tracker.domain.kanban;

import com.issue.tracker.domain.ValidationException;

import java.util.List;

public class BaseKanbanFactory implements KanbanFactory {

    @Override
    public Kanban create(
            String title,
            String description,
            Long ownerId,
            List<Long> admins,
            List<Long> participants
    ) {
        title = sanitizeString(title);
        description = sanitizeString(description);
        validateRequiredString(title);
        if (ownerId == null) {
            throw new ValidationException("The owner is missing");
        }
        return new Kanban(title, description, ownerId, admins, participants);
    }

    String sanitizeString(String value) {
        return value.trim();
    }

    void validateRequiredString(String value) {
        if (value.isEmpty()) {
            throw new ValidationException("String should be non null");
        }
    }
}
