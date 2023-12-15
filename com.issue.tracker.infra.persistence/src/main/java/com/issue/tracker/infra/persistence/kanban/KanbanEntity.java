package com.issue.tracker.infra.persistence.kanban;

import com.issue.tracker.infra.persistence.common.BaseEntity;
import com.issue.tracker.infra.persistence.user.UserEntity;
import jakarta.persistence.*;

import java.util.ArrayList;
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

    @OneToMany(
            mappedBy = "kanban",
            cascade = {
                    CascadeType.DETACH,
                    CascadeType.MERGE,
                    CascadeType.PERSIST,
                    CascadeType.REFRESH,
            }
    )
    private List<KanbanUserEntity> users = new ArrayList<>();

    public KanbanEntity() {
    }

    public KanbanEntity(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public List<UserEntity> getOwners() {
        return users.stream()
                .filter(u -> u.getRole() == KanbanUserRole.OWNER)
                .map(KanbanUserEntity::getUser)
                .toList();
    }

    public List<Long> getOwnerIds() {
        return users.stream()
                .filter(u -> u.getRole() == KanbanUserRole.OWNER)
                .map(u -> u.getUser().getId())
                .toList();
    }

    public List<UserEntity> getParticipants() {
        return users.stream()
                .filter(u -> u.getRole() == KanbanUserRole.PARTICIPANT)
                .map(KanbanUserEntity::getUser)
                .toList();
    }

    public List<Long> getParticipantsIds() {
        return users.stream()
                .filter(u -> u.getRole() == KanbanUserRole.PARTICIPANT)
                .map(u -> u.getUser().getId())
                .toList();
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

    public List<KanbanUserEntity> getUsers() {
        return users;
    }

    public void setUsers(List<KanbanUserEntity> users) {
        this.users = users;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
