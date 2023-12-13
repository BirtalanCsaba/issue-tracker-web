package com.issue.tracker.infra.persistence.kanban;

import com.issue.tracker.infra.persistence.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "kanban")
public class KanbanEntity extends BaseEntity<Long> {
    @Column(unique = true, length = 60, nullable = false)
    private String title;

    @Column(length = 255)
    private String description;

    @OneToMany(mappedBy = "kanban")
    private List<IssueEntity> issues;

    @OneToMany(mappedBy = "user")
    private Set<KanbanUserEntity> users = new HashSet<>();

    public KanbanEntity() {
    }

    public KanbanEntity(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public KanbanEntity(Long aLong, String title, String description) {
        super(aLong);
        this.title = title;
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<IssueEntity> getIssues() {
        return issues;
    }

    public void setIssues(List<IssueEntity> issues) {
        this.issues = issues;
    }

    public Set<KanbanUserEntity> getUsers() {
        return users;
    }

    public void setUsers(Set<KanbanUserEntity> users) {
        this.users = users;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
