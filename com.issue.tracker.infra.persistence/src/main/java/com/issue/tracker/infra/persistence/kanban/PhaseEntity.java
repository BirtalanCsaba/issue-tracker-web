package com.issue.tracker.infra.persistence.kanban;

import com.issue.tracker.infra.persistence.common.BaseEntity;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "phase")
public class PhaseEntity extends BaseEntity<Long> {
    @Column(nullable = false)
    private String rank;

    @Column(nullable = false, length = 30)
    private String title;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "kanban_id", nullable = false)
    private KanbanEntity kanban;

    @OneToMany(
            fetch = FetchType.LAZY,
            mappedBy = "phase",
            cascade = {
                    CascadeType.DETACH,
                    CascadeType.MERGE,
                    CascadeType.PERSIST,
                    CascadeType.REFRESH,
            }
    )
    private List<IssueEntity> issues = new ArrayList<>();

    public PhaseEntity() {

    }

    public PhaseEntity(Long id, String rank, KanbanEntity kanban, String title) {
        this.id = id;
        this.rank = rank;
        this.kanban = kanban;
        this.title = title;
    }

    public PhaseEntity(String rank, KanbanEntity kanban, String title) {
        this.rank = rank;
        this.kanban = kanban;
        this.title = title;
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

    public KanbanEntity getKanban() {
        return kanban;
    }

    public void setKanban(KanbanEntity kanban) {
        this.kanban = kanban;
    }

    public List<IssueEntity> getIssues() {
        return issues;
    }

    public void setIssues(List<IssueEntity> issues) {
        this.issues = issues;
    }
}
