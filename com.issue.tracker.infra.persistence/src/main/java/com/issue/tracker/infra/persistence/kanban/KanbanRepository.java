package com.issue.tracker.infra.persistence.kanban;

import com.issue.tracker.infra.persistence.user.BaseRepository;
import com.issue.tracker.infra.persistence.user.UserEntity;
import jakarta.ejb.Local;

@Local
public interface KanbanRepository extends BaseRepository<KanbanEntity, Long> {
}
