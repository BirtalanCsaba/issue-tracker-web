package com.issue.tracker.domain.kanban;

import java.util.Date;

public interface IssueFactory {
    Issue createIssue(
            String title,
            String description,
            int priority,
            Date expectedDeadline
    );

    Issue createIssue(
            String title,
            String description,
            int priority,
            Date creationDeadline,
            Date expectedDeadline
    );
}
