package com.issue.tracker.infra.persistence.kanban;

import com.issue.tracker.infra.persistence.user.UserEntity;
import jakarta.persistence.*;

import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "kanban_user")
public class KanbanUserEntity implements Serializable {
    @EmbeddedId
    private KanbanUserPK id;

    @ManyToOne
    @MapsId("kanban_id")
    @JoinColumn(name = "kanban_id")
    private KanbanEntity kanban;

    @ManyToOne
    @MapsId("user_id")
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_role", nullable = false)
    private KanbanUserRole role;

    public KanbanUserEntity() {
    }

    public KanbanUserEntity(KanbanUserPK id) {
        this.id = id;
    }

    public KanbanUserPK getId() {
        return id;
    }

    public void setId(KanbanUserPK id) {
        this.id = id;
    }

    public KanbanEntity getKanban() {
        return kanban;
    }

    public void setKanban(KanbanEntity kanban) {
        this.kanban = kanban;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public KanbanUserRole getRole() {
        return role;
    }

    public void setRole(KanbanUserRole role) {
        this.role = role;
    }
}
