package com.issue.tracker.api.kanban;

import com.issue.tracker.api.persistence.kanban.KanbanDsResponseModel;

import java.util.List;

public class UpdateKanbanRequestModel {
    private Long id;

    private String title;

    private String description;

    public UpdateKanbanRequestModel() {
    }

    public UpdateKanbanRequestModel(Long id, String title, String description) {
        this.id = id;
        this.title = title;
        this.description = description;
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
}
