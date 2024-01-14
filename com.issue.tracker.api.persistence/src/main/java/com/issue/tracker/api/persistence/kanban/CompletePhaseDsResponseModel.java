package com.issue.tracker.api.persistence.kanban;

import java.io.Serializable;
import java.util.List;

public class CompletePhaseDsResponseModel implements Serializable {
    private Long id;

    private String rank;

    private String title;

    private Long kanbanId;

    private List<IssueDsResponseModel> issueDsResponseModel;

    public CompletePhaseDsResponseModel() {
    }

    public CompletePhaseDsResponseModel(
            Long id,
            String rank,
            String title,
            Long kanbanId,
            List<IssueDsResponseModel> issueDsResponseModel
    ) {
        this.id = id;
        this.rank = rank;
        this.title = title;
        this.kanbanId = kanbanId;
        this.issueDsResponseModel = issueDsResponseModel;
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

    public Long getKanbanId() {
        return kanbanId;
    }

    public void setKanbanId(Long kanbanId) {
        this.kanbanId = kanbanId;
    }

    public List<IssueDsResponseModel> getIssueDsResponseModel() {
        return issueDsResponseModel;
    }

    public void setIssueDsResponseModel(List<IssueDsResponseModel> issueDsResponseModel) {
        this.issueDsResponseModel = issueDsResponseModel;
    }
}
