package com.issue.tracker.api.kanban;

import jakarta.ejb.Local;

import java.util.List;

@Local
public interface KanbanManagerInput {
    KanbanResponseModel create(CreateKanbanRequestModel kanban);

    void update(UpdateKanbanRequestModel kanban, Long userId);

    List<EnrolledKanbanResponseModel> findAllEnrolledKanbansForUser(Long userId);

    void removeKanbanById(Long userId, Long kanbanId);

    PhaseResponseModel addPhase(Long userId, Long kanbanId, String title);

    void insertPhaseBetween(Long userId, InsertPhaseRequestModel phaseRequestModel);

    void reIndex(Long kanbanId);

    KanbanResponseModel findById(Long kanbanId);

    KanbanCompleteResponseModel findCompleteById(Long kanbanId);

    void addLastPhase(Long userId, MovePhaseRequestModel phase);

    void addFirstPhase(Long userId, MovePhaseRequestModel phase);

    List<PhaseResponseModel> findAllPhasesInOrder(Long userId, Long kanbanId);

    IssueResponseModel createIssue(Long userId, CreateIssueRequestModel issueRequestModel);
}
