package com.issue.tracker.api.kanban;

import java.io.Serializable;
import java.util.Date;

public class UpdateIssueRequestModel implements Serializable {
    private Long issueId;

    private String title;

    private String description;

    private int priority;

    private Date expectedDeadline;

    private Long phaseId;

    private Long assignedUser;

    public UpdateIssueRequestModel() {
    }

    public UpdateIssueRequestModel(
            Long issueId,
            String title,
            String description,
            int priority,
            Date expectedDeadline,
            Long phaseId
    ) {
        this.issueId = issueId;
        this.title = title;
        this.description = description;
        this.priority = priority;
        this.expectedDeadline = expectedDeadline;
        this.phaseId = phaseId;
    }

    public UpdateIssueRequestModel(
            Long issueId,
            String title,
            String description,
            int priority,
            Date expectedDeadline,
            Long phaseId,
            Long assignedUser
    ) {
        this.issueId = issueId;
        this.title = title;
        this.description = description;
        this.priority = priority;
        this.expectedDeadline = expectedDeadline;
        this.phaseId = phaseId;
        this.assignedUser = assignedUser;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public Date getExpectedDeadline() {
        return expectedDeadline;
    }

    public void setExpectedDeadline(Date expectedDeadline) {
        this.expectedDeadline = expectedDeadline;
    }

    public Long getPhaseId() {
        return phaseId;
    }

    public void setPhaseId(Long phaseId) {
        this.phaseId = phaseId;
    }

    public Long getAssignedUser() {
        return assignedUser;
    }

    public void setAssignedUser(Long assignedUser) {
        this.assignedUser = assignedUser;
    }

    public Long getIssueId() {
        return issueId;
    }

    public void setIssueId(Long issueId) {
        this.issueId = issueId;
    }
}
