package com.issue.tracker.api.kanban;

import com.issue.tracker.api.persistence.kanban.IssueDsResponseModel;

import java.io.Serializable;
import java.util.List;

public class CompletePhaseResponseModel implements Serializable {
    private Long id;

    private String rank;

    private String title;

    private Long kanbanId;

    private List<IssueResponseModel> issue;

    public CompletePhaseResponseModel() {
    }

    public CompletePhaseResponseModel(Long id, String rank, String title, Long kanbanId, List<IssueResponseModel> issue) {
        this.id = id;
        this.rank = rank;
        this.title = title;
        this.kanbanId = kanbanId;
        this.issue = issue;
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

    public List<IssueResponseModel> getIssue() {
        return issue;
    }

    public void setIssue(List<IssueResponseModel> issue) {
        this.issue = issue;
    }
}
