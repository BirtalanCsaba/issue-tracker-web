package com.issue.tracker.api.persistence.kanban;

import java.io.Serializable;

public class CreatePhaseRequestModel implements Serializable {
    private Long kanbanId;

    private String rank;

    private String title;

    public CreatePhaseRequestModel() {

    }

    public CreatePhaseRequestModel(Long kanbanId, String rank, String title) {
        this.kanbanId = kanbanId;
        this.rank = rank;
        this.title = title;
    }

    public Long getKanbanId() {
        return kanbanId;
    }

    public void setKanbanId(Long kanbanId) {
        this.kanbanId = kanbanId;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
