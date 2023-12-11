package com.issue.tracker.api.kanban;

import jakarta.ejb.Local;

@Local
public interface IssueManagerInput {
    IssueResponseModel createIssue(CreateIssueRequestModel issue);
}
