package com.issue.tracker.api.kanban;

import jakarta.ejb.Stateless;

@Stateless
public class IssueInteractor implements IssueManagerInput {
    @Override
    public IssueResponseModel createIssue(CreateIssueRequestModel issue) {
        return null;
    }
}
