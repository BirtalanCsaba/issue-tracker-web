package com.issue.tracker.api.persistence.kanban;

import java.util.Date;
import java.io.Serializable;

public class UpdateIssueDsRequestModel implements Serializable{
    private Long id;

    private String title;

    private String description;

    private int priority;

    private Date expectedDeadline;

    private Long assignedUser;

    private Long phaseId;

    public UpdateIssueDsRequestModel() {
    }

    public UpdateIssueDsRequestModel(
            Long id,
            String title,
            String description,
            int priority,
            Date expectedDeadline,
            Long assignedUser,
            Long phaseId
    ) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.priority = priority;
        this.expectedDeadline = expectedDeadline;
        this.assignedUser = assignedUser;
        this.phaseId = phaseId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Long getAssignedUser() {
        return assignedUser;
    }

    public void setAssignedUser(Long assignedUser) {
        this.assignedUser = assignedUser;
    }

    public Long getPhaseId() {
        return phaseId;
    }

    public void setPhaseId(Long phaseId) {
        this.phaseId = phaseId;
    }
}
