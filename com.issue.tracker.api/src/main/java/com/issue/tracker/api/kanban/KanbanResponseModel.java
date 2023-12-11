package com.issue.tracker.api.kanban;

public class KanbanResponseModel {
    private Long id;

    private String title;

    public KanbanResponseModel() {
    }

    public KanbanResponseModel(Long id, String title) {
        this.id = id;
        this.title = title;
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
}
