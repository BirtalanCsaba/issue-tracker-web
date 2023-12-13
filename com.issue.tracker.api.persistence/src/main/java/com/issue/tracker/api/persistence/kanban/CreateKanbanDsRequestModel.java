package com.issue.tracker.api.persistence.kanban;

import java.util.List;

public class CreateKanbanDsRequestModel {
    private String title;

    private String description;

    private Long ownerId;

    private List<Long> participants;

    public CreateKanbanDsRequestModel() {

    }

    public CreateKanbanDsRequestModel(String title, String description, Long ownerId, List<Long> participants) {
        this.title = title;
        this.description = description;
        this.ownerId = ownerId;
        this.participants = participants;
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

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public List<Long> getParticipants() {
        return participants;
    }

    public void setParticipants(List<Long> participants) {
        this.participants = participants;
    }
}
