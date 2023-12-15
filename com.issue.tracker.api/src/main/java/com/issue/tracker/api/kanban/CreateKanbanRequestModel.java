package com.issue.tracker.api.kanban;

import java.util.List;

public class CreateKanbanRequestModel {
    private String title;

    private String description;

    private Long ownerId;

    private List<Long> admins;

    private List<Long> participants;

    public CreateKanbanRequestModel() {
    }

    public CreateKanbanRequestModel(String title, String description, Long ownerId, List<Long> admins, List<Long> participants) {
        this.title = title;
        this.description = description;
        this.ownerId = ownerId;
        this.admins = admins;
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

    public List<Long> getAdmins() {
        return admins;
    }

    public void setAdmins(List<Long> admins) {
        this.admins = admins;
    }

    public List<Long> getParticipants() {
        return participants;
    }

    public void setParticipants(List<Long> participants) {
        this.participants = participants;
    }
}
