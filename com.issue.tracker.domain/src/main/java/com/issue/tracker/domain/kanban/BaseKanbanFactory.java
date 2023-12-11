package com.issue.tracker.domain.kanban;

import com.issue.tracker.domain.ValidationException;

public class BaseKanbanFactory implements KanbanFactory {

    @Override
    public Kanban create(String title) {
        title = sanitizeString(title);
        validateRequiredString(title);
        return new Kanban(title);
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
