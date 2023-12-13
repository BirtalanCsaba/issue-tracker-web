package com.issue.tracker.infra.persistence.kanban;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;

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
