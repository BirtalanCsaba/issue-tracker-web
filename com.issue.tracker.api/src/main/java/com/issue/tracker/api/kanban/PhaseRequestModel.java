package com.issue.tracker.api.kanban;

import java.io.Serializable;

public class PhaseRequestModel implements Serializable {
    private String title;

    public PhaseRequestModel() {

    }

    public PhaseRequestModel(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
