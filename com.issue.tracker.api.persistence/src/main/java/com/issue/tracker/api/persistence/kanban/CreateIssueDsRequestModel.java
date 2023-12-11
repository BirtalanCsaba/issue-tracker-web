package com.issue.tracker.api.persistence.kanban;

import java.util.Date;

public class CreateIssueDsRequestModel {
    private String title;

    private String description;

    private int priority;

    private Date creationTimestamp;

    private Date expectedDeadline;

    public CreateIssueDsRequestModel() {
    }

    public CreateIssueDsRequestModel(String title, String description, int priority, Date creationTimestamp, Date expectedDeadline) {
        this.title = title;
        this.description = description;
        this.priority = priority;
        this.creationTimestamp = creationTimestamp;
        this.expectedDeadline = expectedDeadline;
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

    public Date getCreationTimestamp() {
        return creationTimestamp;
    }

    public void setCreationTimestamp(Date creationTimestamp) {
        this.creationTimestamp = creationTimestamp;
    }

    public Date getExpectedDeadline() {
        return expectedDeadline;
    }

    public void setExpectedDeadline(Date expectedDeadline) {
        this.expectedDeadline = expectedDeadline;
    }
}
