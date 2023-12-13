package com.issue.tracker.api.kanban;

import com.issue.tracker.api.persistence.kanban.CreateIssueDsRequestModel;
import com.issue.tracker.api.persistence.kanban.IssueDsGateway;
import com.issue.tracker.api.persistence.kanban.IssueDsResponseModel;
import com.issue.tracker.domain.kanban.BaseIssueFactory;
import com.issue.tracker.domain.kanban.Issue;
import com.issue.tracker.domain.kanban.IssueFactory;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;

@Stateless
public class IssueInteractor implements IssueManagerInput {

    @EJB
    private IssueDsGateway issueDsGateway;

    private final IssueFactory issueFactory = new BaseIssueFactory();

    @Override
    public IssueResponseModel createIssue(CreateIssueRequestModel issue) {
        Issue newIssue = issueFactory.createIssue(
                issue.getTitle(),
                issue.getDescription(),
                issue.getPriority(),
                issue.getExpectedDeadline()
        );
        IssueDsResponseModel response = issueDsGateway.create(new CreateIssueDsRequestModel(
                newIssue.getTitle(),
                newIssue.getDescription(),
                newIssue.getPriority(),
                newIssue.getCreationTimestamp(),
                newIssue.getExpectedDeadline()
        ));
        return new IssueResponseModel(
                response.getId(),
                response.getTitle(),
                response.getDescription(),
                response.getPriority(),
                response.getCreationTimestamp(),
                response.getExpectedDeadline()
        );
    }
}
