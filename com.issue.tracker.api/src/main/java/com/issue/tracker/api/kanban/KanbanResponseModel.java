package com.issue.tracker.api.kanban;

import java.util.List;

public class KanbanResponseModel {
    private Long id;

    private String title;

    private String description;

    private List<Long> owners;

    private List<Long> participants;

    public KanbanResponseModel() {
    }

    public KanbanResponseModel(Long id, String title, String description, List<Long> owners, List<Long> participants) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.owners = owners;
        this.participants = participants;
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

    public List<Long> getOwnerId() {
        return owners;
    }

    public void setOwnerId(List<Long> owners) {
        this.owners = owners;
    }

    public List<Long> getParticipants() {
        return participants;
    }

    public void setParticipants(List<Long> participants) {
        this.participants = participants;
    }
}
