package com.issue.tracker.api.persistence.kanban;

public class UpdateKanbanDsRequestModel {
    private Long id;

    private String title;

    private String description;

    public UpdateKanbanDsRequestModel() {
    }

    public UpdateKanbanDsRequestModel(Long id, String title, String description) {
        this.id = id;
        this.title = title;
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
