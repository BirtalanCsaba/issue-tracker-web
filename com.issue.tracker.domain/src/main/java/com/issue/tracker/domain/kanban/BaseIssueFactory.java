package com.issue.tracker.domain.kanban;

import com.issue.tracker.domain.ValidationException;

import java.util.Date;

public class BaseIssueFactory implements IssueFactory {

    @Override
    public Issue createIssue(
            String title,
            String description,
            int priority,
            Date expectedDeadline
    ) {
        title = sanitizeString(title);
        description = sanitizeString(description);
        validatePriority(priority);
        validateRequiredString(title);
        Date creationDate = new Date();
        validateDeadline(expectedDeadline);
        return new Issue(title, description, priority, creationDate, expectedDeadline);
    }

    @Override
    public Issue createIssue(String title, String description, int priority, Date creationDeadline, Date expectedDeadline) {
        title = sanitizeString(title);
        description = sanitizeString(description);
        validatePriority(priority);
        validateRequiredString(title);
        Date creationDate = new Date();
        validateDeadline(creationDeadline, expectedDeadline);
        return new Issue(title, description, priority, creationDate, expectedDeadline);
    }

    String sanitizeString(String value) {
        return value.trim();
    }

    void validateRequiredString(String value) {
        if (value.isEmpty()) {
            throw new ValidationException("String should be non null");
        }
    }

    void validatePriority(int priority) {
        if (priority < 0 || priority > 5) {
            throw new ValidationException("Priority should have values between 0 and 5");
        }
    }

    void validateDeadline(Date deadline) {
        if (deadline.before(new Date())) {
            throw new ValidationException("Expected deadline is before now()");
        }
    }

    void validateDeadline(Date deadline, Date creationDate) {
        if (deadline.before(creationDate)) {
            throw new ValidationException("Expected deadline is before creationDate()");
        }
    }
}
