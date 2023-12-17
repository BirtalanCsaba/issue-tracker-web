package com.issue.tracker.infra.persistence.kanban;

import com.issue.tracker.infra.persistence.common.BaseEntity;
import com.issue.tracker.infra.persistence.user.UserEntity;
import jakarta.persistence.*;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "issue")
public class IssueEntity extends BaseEntity<Long> {
    @Column(length = 70, nullable = false)
    private String title;

    @Column(length = 255)
    private String description;

    @Column(nullable = false)
    private Integer priority;

    @Column(nullable = false)
    private Date creationTimestamp = new Date();

    @Column(nullable = false)
    private Date expectedDeadline;

    @ManyToOne
    @JoinColumn(name="kanban_id", nullable=false)
    private KanbanEntity kanban;

    public IssueEntity() {
    }

    public IssueEntity(Long aLong, String title, String description, Integer priority, Date expectedDeadline) {
        super(aLong);
        this.title = title;
        this.description = description;
        this.priority = priority;
        this.expectedDeadline = expectedDeadline;
    }

    public IssueEntity(Long aLong, String title, String description, Integer priority, Date creationTimestamp, Date expectedDeadline) {
        super(aLong);
        this.title = title;
        this.description = description;
        this.priority = priority;
        this.creationTimestamp = creationTimestamp;
        this.expectedDeadline = expectedDeadline;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public Date getCreationTimestamp() {
        return creationTimestamp;
    }

    public void setCreationTimestamp(Date creationTimestamp) {
        this.creationTimestamp = creationTimestamp;
    }

    public Date getExpectedDeadline() {
        return expectedDeadline;
    }

    public void setExpectedDeadline(Date expectedDeadline) {
        this.expectedDeadline = expectedDeadline;
    }

    public KanbanEntity getKanban() {
        return kanban;
    }

    public void setKanban(KanbanEntity kanban) {
        this.kanban = kanban;
    }
}
