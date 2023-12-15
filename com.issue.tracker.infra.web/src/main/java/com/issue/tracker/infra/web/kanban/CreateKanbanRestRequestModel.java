package com.issue.tracker.infra.web.kanban;

import java.util.List;

public class CreateKanbanRestRequestModel {
    private String title;

    private String description;

    private List<Long> participants;

    public CreateKanbanRestRequestModel() {
    }

    public CreateKanbanRestRequestModel(String title, String description, List<Long> participants) {
        this.title = title;
        this.description = description;
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

    public List<Long> getParticipants() {
        return participants;
    }

    public void setParticipants(List<Long> participants) {
        this.participants = participants;
    }
}
