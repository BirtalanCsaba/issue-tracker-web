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

    @ManyToOne(
            cascade = {
                    CascadeType.ALL
            }
    )
    @JoinColumn(name="phase_id", nullable=false)
    private PhaseEntity phase;

    @ManyToOne
    @JoinColumn(name="assgined_user")
    private UserEntity assignedUser;

    public IssueEntity() {
    }

    public IssueEntity(
            String title,
            String description,
            Integer priority,
            Date expectedDeadline,
            PhaseEntity phase
    ) {
        this.title = title;
        this.description = description;
        this.priority = priority;
        this.expectedDeadline = expectedDeadline;
        this.phase = phase;
    }

    public IssueEntity(
            String title,
            String description,
            Integer priority,
            Date expectedDeadline,
            PhaseEntity phase,
            UserEntity assignedUser
    ) {
        this.title = title;
        this.description = description;
        this.priority = priority;
        this.expectedDeadline = expectedDeadline;
        this.phase = phase;
        this.assignedUser = assignedUser;
    }

    public IssueEntity(
            Long aLong,
            String title,
            String description,
            Integer priority,
            Date creationTimestamp,
            Date expectedDeadline,
            PhaseEntity phase
    ) {
        super(aLong);
        this.title = title;
        this.description = description;
        this.priority = priority;
        this.creationTimestamp = creationTimestamp;
        this.expectedDeadline = expectedDeadline;
        this.phase = phase;
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

    public PhaseEntity getPhase() {
        return phase;
    }

    public void setPhase(PhaseEntity phase) {
        this.phase = phase;
    }

    public UserEntity getAssignedUser() {
        return assignedUser;
    }

    public void setAssignedUser(UserEntity assignedUser) {
        this.assignedUser = assignedUser;
    }
}
