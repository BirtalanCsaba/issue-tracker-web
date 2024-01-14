package com.issue.tracker.api.persistence.kanban;

import java.io.Serializable;
import java.util.Date;

public class IssueDsResponseModel implements Serializable {
    private Long id;

    private String title;

    private String description;

    private int priority;

    private Date creationTimestamp;

    private Date expectedDeadline;

    private Long phaseId;

    public IssueDsResponseModel() {
    }

    public IssueDsResponseModel(Long id, String title, String description, int priority, Date creationTimestamp, Date expectedDeadline, Long phaseId) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.priority = priority;
        this.creationTimestamp = creationTimestamp;
        this.expectedDeadline = expectedDeadline;
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

    public Long getPhaseId() {
        return phaseId;
    }

    public void setPhaseId(Long phaseId) {
        this.phaseId = phaseId;
    }
}
