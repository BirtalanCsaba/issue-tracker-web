package com.issue.tracker.domain.kanban;

public class Kanban {
    private String title;

    public Kanban(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
