package com.issue.tracker.api.persistence.kanban;

import com.issue.tracker.api.persistence.common.OrderingType;
import jakarta.ejb.Remote;

import java.util.List;

@Remote
public interface KanbanDsGateway {
    KanbanDsResponseModel create(CreateKanbanDsRequestModel kanban);

    KanbanDsResponseModel findById(Long id);

    KanbanDsResponseModel findByTitle(String title);

    void update(UpdateKanbanDsRequestModel kanban);

    List<EnrolledKanbanDsResponseModel> findAllByUserId(Long userId);

    boolean isOwner(Long userId, Long kanbanId);

    boolean isAdmin(Long userId, Long kanbanId);

    boolean isParticipant(Long userId, Long kanbanId);

    void removeById(Long id);

    long getPhaseCount(Long kanbanId);

    void updatePhase(Long phaseId, String title, String rank);

    PhaseDsResponseModel findFirstPhase(Long kanbanId);

    List<PhaseDsResponseModel> findAllPhasesForKanban(Long kanbanId);

    List<PhaseDsResponseModel> findAllPhasesForKanbanOrdered(Long kanbanId, OrderingType order);

    PhaseDsResponseModel addPhase(CreatePhaseRequestModel phase);

    void updatePhases(List<UpdatePhaseRequestModel> phases);
}
