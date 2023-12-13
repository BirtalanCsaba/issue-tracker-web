package com.issue.tracker.infra.persistence.kanban;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class KanbanUserPK implements Serializable {
    @Column(name = "user_d")
    private Long userId;

    @Column(name = "kanban_id")
    private Long kanbanId;

    public KanbanUserPK() {
    }

    public KanbanUserPK(Long userId, Long kanbanId) {
        this.userId = userId;
        this.kanbanId = kanbanId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass())
            return false;

        KanbanUserPK that = (KanbanUserPK) o;
        return Objects.equals(userId, that.userId) &&
                Objects.equals(kanbanId, that.kanbanId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, kanbanId);
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getKanbanId() {
        return kanbanId;
    }

    public void setKanbanId(Long kanbanId) {
        this.kanbanId = kanbanId;
    }
}
