package com.issue.tracker.api.kanban;

import java.io.Serializable;
import java.util.Date;

public class CreateIssueRequestModel implements Serializable {

    private String title;

    private String description;

    private int priority;

    private Date expectedDeadline;

    private Long phaseId;

    public CreateIssueRequestModel() {
    }

    public CreateIssueRequestModel(String title, String description, int priority, Date expectedDeadline, Long phaseId) {
        this.title = title;
        this.description = description;
        this.priority = priority;
        this.expectedDeadline = expectedDeadline;
        this.phaseId = phaseId;
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
}
