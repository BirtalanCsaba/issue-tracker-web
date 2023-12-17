package com.issue.tracker.infra.persistence.kanban;

import com.issue.tracker.infra.persistence.user.BaseRepository;
import com.issue.tracker.infra.persistence.user.UserEntity;
import jakarta.ejb.Local;

import java.util.List;

@Local
public interface KanbanRepository extends BaseRepository<KanbanEntity, Long> {
    List<KanbanEntity> findOwningKanbans(Long userId);

    List<KanbanUserEntity> findEnrollments(Long userId);

    KanbanEntity findByTitle(String title);

    boolean isOwner(Long userId, Long kanbanId);

    boolean isAdmin(Long userId, Long kanbanId);

    boolean isParticipant(Long userId, Long kanbanId);
}
