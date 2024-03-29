package com.issue.tracker.infra.persistence.kanban;

import com.issue.tracker.infra.persistence.common.BaseEntity;
import com.issue.tracker.infra.persistence.user.UserEntity;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@NamedEntityGraph(
        name = "user-participant",
        attributeNodes = {
                @NamedAttributeNode("users"),
                @NamedAttributeNode("owner"),
        }
)
@Entity
@Table(name = "kanban")
public class KanbanEntity extends BaseEntity<Long> {
    @Column(unique = true, length = 60, nullable = false)
    private String title;

    @Column(length = 255)
    private String description;

    @OneToMany(
            mappedBy = "kanban",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<KanbanUserEntity> users = new ArrayList<>();

    @ManyToOne(
            fetch = FetchType.LAZY,
            cascade = {
                    CascadeType.DETACH,
                    CascadeType.MERGE,
                    CascadeType.PERSIST,
                    CascadeType.REFRESH,
            }
    )
    @JoinColumn(name = "owner_id", nullable = false)
    private UserEntity owner;

    @OneToMany(mappedBy = "kanban")
    private List<PhaseEntity> phase = new ArrayList<>();

    public KanbanEntity() {
    }

    public KanbanEntity(String title, String description, UserEntity owner) {
        this.title = title;
        this.description = description;
        this.owner = owner;
    }

    public KanbanEntity(Long aLong, String title, String description) {
        super(aLong);
        this.title = title;
        this.description = description;
    }

    public KanbanEntity(Long aLong, String title, String description, UserEntity owner) {
        super(aLong);
        this.title = title;
        this.description = description;
        this.owner = owner;
    }

    public List<UserEntity> getAdmins() {
        return users.stream()
                .filter(u -> u.getRole() == KanbanUserRole.ADMIN)
                .map(KanbanUserEntity::getUser)
                .toList();
    }

    public List<Long> getAdminIds() {
        return users.stream()
                .filter(u -> u.getRole() == KanbanUserRole.ADMIN)
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public UserEntity getOwner() {
        return owner;
    }

    public void setOwner(UserEntity owner) {
        this.owner = owner;
    }

    public List<PhaseEntity> getPhase() {
        return phase;
    }

    public void setPhase(List<PhaseEntity> phase) {
        this.phase = phase;
    }


}
