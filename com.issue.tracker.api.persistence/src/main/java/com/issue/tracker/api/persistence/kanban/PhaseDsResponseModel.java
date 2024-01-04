package com.issue.tracker.api.persistence.kanban;

import java.io.Serializable;

public class PhaseDsResponseModel implements Serializable {
    private Long id;

    private String rank;

    private String title;

    private Long kanbanId;

    public PhaseDsResponseModel() {

    }

    public PhaseDsResponseModel(Long id, String rank, String title, Long kanbanId) {
        this.id = id;
        this.rank = rank;
        this.title = title;
        this.kanbanId = kanbanId;
    }

    public Long getKanbanId() {
        return kanbanId;
    }

    public void setKanbanId(Long kanbanId) {
        this.kanbanId = kanbanId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
