package com.issue.tracker.api.kanban;

import java.util.List;

public class KanbanResponseModel {
    private Long id;

    private String title;

    private String description;

    private List<Long> admins;

    private List<Long> participants;

    private Long ownerId;

    public KanbanResponseModel() {
    }

    public KanbanResponseModel(Long id, String title, String description, Long ownerId, List<Long> admins, List<Long> participants) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.admins = admins;
        this.participants = participants;
        this.ownerId = ownerId;
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

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }
}
