package com.issue.tracker.api.persistence.kanban;

public class CreateKanbanDsRequestModel {
    private String title;

    public CreateKanbanDsRequestModel() {

    }

    public CreateKanbanDsRequestModel(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
