package com.issue.tracker.api.kanban;

public class CreateKanbanRequestModel {
    private String title;

    public CreateKanbanRequestModel() {
    }

    public CreateKanbanRequestModel(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
