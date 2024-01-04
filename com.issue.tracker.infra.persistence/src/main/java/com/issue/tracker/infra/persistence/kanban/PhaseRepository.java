package com.issue.tracker.infra.persistence.kanban;

import com.issue.tracker.api.persistence.common.OrderingType;
import com.issue.tracker.infra.persistence.user.BaseRepository;
import jakarta.ejb.Local;

import java.util.List;

@Local
public interface PhaseRepository extends BaseRepository<PhaseEntity, Long> {
    PhaseEntity findLastPhaseForKanban(Long kanbanId);

    PhaseEntity findFirstPhaseForKanban(Long kanbanId);

    PhaseEntity getNthElemAfterSort(Long kanbanId, long index);

    List<PhaseEntity> findAllPhasesForKanbanOrdered(Long kanbanId, OrderingType order);
}
