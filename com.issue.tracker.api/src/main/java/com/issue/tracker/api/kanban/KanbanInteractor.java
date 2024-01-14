package com.issue.tracker.api.kanban;

import com.issue.tracker.api.ApiException;
import com.issue.tracker.api.auth.UserNotAuthorizedException;
import com.issue.tracker.api.common.BaseConversionUtil;
import com.issue.tracker.api.common.RankUtils;
import com.issue.tracker.api.persistence.common.OrderingType;
import com.issue.tracker.api.persistence.kanban.*;
import com.issue.tracker.domain.kanban.BaseKanbanFactory;
import com.issue.tracker.domain.kanban.Kanban;
import com.issue.tracker.domain.kanban.KanbanFactory;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.transaction.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Stateless
public class KanbanInteractor implements KanbanManagerInput {
    @EJB
    private KanbanDsGateway kanbanDsGateway;

    private final KanbanFactory kanbanFactory = new BaseKanbanFactory();

    @Override
    public KanbanResponseModel create(CreateKanbanRequestModel kanban) {
        Kanban newKanban = kanbanFactory.create(
                kanban.getTitle(),
                kanban.getDescription(),
                kanban.getOwnerId(),
                kanban.getAdmins(),
                kanban.getParticipants()
        );
        KanbanDsResponseModel response = kanbanDsGateway.create(new CreateKanbanDsRequestModel(
                newKanban.getTitle(),
                newKanban.getDescription(),
                newKanban.getOwner(),
                kanban.getAdmins(),
                newKanban.getParticipants()
        ));
        return new KanbanResponseModel(
                response.getId(),
                response.getTitle(),
                response.getDescription(),
                response.getOwnerId(),
                response.getAdmins(),
                response.getParticipants()
        );
    }

    @Override
    public void update(UpdateKanbanRequestModel kanban, Long userId) {
        if (!kanbanDsGateway.isAdmin(userId, kanban.getId())) {
            throw new UserNotAuthorizedException("User with id: " + userId + " is not the owner of the kanban with id: " + kanban.getId());
        }
        kanbanDsGateway.update(new UpdateKanbanDsRequestModel(
                kanban.getId(),
                kanban.getTitle(),
                kanban.getDescription()
        ));
    }

    @Override
    public List<EnrolledKanbanResponseModel> findAllEnrolledKanbansForUser(Long userId) {
        List<EnrolledKanbanDsResponseModel> kanbans = kanbanDsGateway.findAllByUserId(userId);
        return kanbans.stream()
                .map(k -> new EnrolledKanbanResponseModel(
                        k.getId(),
                        k.getTitle(),
                        k.getDescription(),
                        k.getOwnerId(),
                        k.getAdmins(),
                        k.getParticipants(),
                        k.getRole()
                )).toList();
    }

    @Override
    public void removeKanbanById(Long userId, Long kanbanId) {
        if (kanbanId == null) {
            throw new ApiException("Kanban ID is null");
        }
        if (!kanbanDsGateway.isOwner(userId, kanbanId)) {
            throw new UserNotAuthorizedException("User with id: " + userId + " is not the owner of the kanban with id: " + kanbanId);
        }
        kanbanDsGateway.removeById(kanbanId);
    }

    @Override
    public PhaseResponseModel addPhase(Long userId, Long kanbanId, String title) {
        if (kanbanId == null) {
            throw new ApiException("Kanban ID is null");
        }
        if (userId == null) {
            throw new ApiException("User ID is null");
        }
        if (!kanbanDsGateway.isOwner(userId, kanbanId)) {
            if (!kanbanDsGateway.isAdmin(userId, kanbanId)) {
                throw new UserNotAuthorizedException("User with id: " + userId + " is not the admin of the kanban with id: " + kanbanId);
            }
        }

        long phaseCount = kanbanDsGateway.getPhaseCount(kanbanId);

        PhaseDsResponseModel createdPhase;
        if (phaseCount == 0) {
            createdPhase = kanbanDsGateway.addPhase(new CreatePhaseRequestModel(
                    kanbanId,
                    "0|" + BaseConversionUtil.MIN_VALUE,
                    title
            ));
        } else {
            PhaseDsResponseModel firstPhase = kanbanDsGateway.findFirstPhase(kanbanId);
            int bucket = getBucket(firstPhase.getRank());
            String newRank = String.valueOf(bucket) + "|" + BaseConversionUtil.MAX_VALUE;
            createdPhase = kanbanDsGateway.addPhase(new CreatePhaseRequestModel(
                    kanbanId,
                    newRank,
                    title
            ));
            reIndex(kanbanId);
        }

        createdPhase = kanbanDsGateway.findPhaseById(createdPhase.getId());

        return new PhaseResponseModel(
                createdPhase.getId(),
                createdPhase.getRank(),
                createdPhase.getTitle()
        );
    }

    @Override
    public void insertPhaseBetween(Long userId, InsertPhaseRequestModel phaseRequestModel) {
        var toBeInsertedPhase = kanbanDsGateway.findPhaseById(phaseRequestModel.getToBeInserted());
        if (toBeInsertedPhase == null) {
            throw new PhaseNotFoundException("Phase not found");
        }

        if (!kanbanDsGateway.isOwner(userId, toBeInsertedPhase.getKanbanId())) {
            if (!kanbanDsGateway.isAdmin(userId, toBeInsertedPhase.getKanbanId())) {
                throw new UserNotAuthorizedException("User with id: " + userId + " is not the admin of the kanban with id: " + toBeInsertedPhase.getKanbanId());
            }
        }

        var firstPhase = kanbanDsGateway.findPhaseById(phaseRequestModel.getFirstPhase());
        if (firstPhase == null) {
            throw new PhaseNotFoundException("Phase not found");
        }

        var secondPhase = kanbanDsGateway.findPhaseById(phaseRequestModel.getSecondPhase());
        if (secondPhase == null) {
            throw new PhaseNotFoundException("Phase not found");
        }

        if (!Objects.equals(toBeInsertedPhase.getKanbanId(), firstPhase.getKanbanId()) && toBeInsertedPhase.getKanbanId() != secondPhase.getKanbanId()) {
            throw new ApiException("Provided phases are not from the same kanban");
        }

        Optional<String> optionalNewRank = RankUtils.calculateMiddleRank(getRank(firstPhase.getRank()), getRank(secondPhase.getRank()));
        if (optionalNewRank.isEmpty()) {
            reIndex(toBeInsertedPhase.getKanbanId());

            // find after reindexing
            firstPhase = kanbanDsGateway.findPhaseById(phaseRequestModel.getFirstPhase());
            secondPhase = kanbanDsGateway.findPhaseById(phaseRequestModel.getSecondPhase());
        }

        // it is for sure good
        String actualNewRank = RankUtils.calculateMiddleRank(getRank(firstPhase.getRank()), getRank(secondPhase.getRank())).get();

        String newRank = String.valueOf(getBucket(firstPhase.getRank())) + "|" + actualNewRank;

        kanbanDsGateway.updatePhase(
                toBeInsertedPhase.getId(),
                toBeInsertedPhase.getTitle(),
                newRank
        );
    }

    @Override
    public void reIndex(Long kanbanId) {
        PhaseDsResponseModel phase = kanbanDsGateway.findFirstPhase(kanbanId);
        int currentBucket = getBucket(phase.getRank());
        int nextBucket = getNextBucket(currentBucket);

        List<PhaseDsResponseModel> phases;

        phases = kanbanDsGateway.findAllPhasesForKanbanOrdered(kanbanId, OrderingType.ASCENDING);

        long rankingSize = phases.size();

        BaseConversionUtil.calculateDefaultRanks(rankingSize);

        int index = 0;
        for (String defaultRank : BaseConversionUtil.DEFAULT_RANKS) {
            if (index >= phases.size() || index < 0) {
                break;
            }
            String newRank = String.valueOf(nextBucket) + "|" + defaultRank;
            phases.get(index).setRank(newRank);
            index++;
        }

        List<UpdatePhaseRequestModel> updatePhases = phases.stream().map(p -> new UpdatePhaseRequestModel(
                p.getId(),
                p.getRank(),
                p.getTitle()
        )).toList();
        kanbanDsGateway.updatePhases(updatePhases);
    }

    @Override
    public KanbanResponseModel findById(Long kanbanId) {
        var response = kanbanDsGateway.findById(kanbanId);
        return new KanbanResponseModel(
                response.getId(),
                response.getTitle(),
                response.getDescription(),
                response.getOwnerId(),
                response.getAdmins(),
                response.getParticipants()
        );
    }

    @Override
    public KanbanCompleteResponseModel findCompleteById(Long kanbanId) {
        var response = kanbanDsGateway.findCompleteById(kanbanId);
        return new KanbanCompleteResponseModel(
                response.getId(),
                response.getTitle(),
                response.getDescription(),
                response.getAdmins(),
                response.getParticipants(),
                response.getOwner(),
                response.getPhase().stream().map(p -> new CompletePhaseResponseModel(
                        p.getId(),
                        p.getRank(),
                        p.getTitle(),
                        p.getKanbanId(),
                        p.getIssueDsResponseModel().stream().map(i -> new IssueResponseModel(
                                i.getId(),
                                i.getTitle(),
                                i.getDescription(),
                                i.getPriority(),
                                i.getCreationTimestamp(),
                                i.getExpectedDeadline(),
                                i.getPhaseId()
                        )).toList()
                )).toList()
        );
    }

    @Override
    public void removePhase(Long userId, Long phaseId) {
        var toBeInsertedPhase = kanbanDsGateway.findPhaseById(phaseId);
        if (toBeInsertedPhase == null) {
            throw new PhaseNotFoundException("Phase not found");
        }
        if (!kanbanDsGateway.isOwner(userId, toBeInsertedPhase.getKanbanId())) {
            if (!kanbanDsGateway.isAdmin(userId, toBeInsertedPhase.getKanbanId())) {
                throw new UserNotAuthorizedException("User with id: " + userId + " is not the admin of the kanban with id: " + toBeInsertedPhase.getKanbanId());
            }
        }
        kanbanDsGateway.deletePhase(phaseId);
    }

    @Override
    public void removeIssue(Long userId, Long issueId) {
        var issue = kanbanDsGateway.findIssueById(issueId);
        if (issue == null) {
            return;
        }
        var toBeInsertedPhase = kanbanDsGateway.findPhaseById(issue.getPhaseId());
        if (!kanbanDsGateway.isOwner(userId, toBeInsertedPhase.getKanbanId())) {
            if (!kanbanDsGateway.isAdmin(userId, toBeInsertedPhase.getKanbanId())) {
                throw new UserNotAuthorizedException("User with id: " + userId + " is not the admin of the kanban with id: " + toBeInsertedPhase.getKanbanId());
            }
        }
        kanbanDsGateway.deleteIssue(issueId);
    }

    @Override
    @Transactional
    public void addLastPhase(Long userId, MovePhaseRequestModel phase) {
        var toBeInsertedPhase = kanbanDsGateway.findPhaseById(phase.getToBeInserted());
        if (toBeInsertedPhase == null) {
            throw new PhaseNotFoundException("Phase not found");
        }
        if (!kanbanDsGateway.isOwner(userId, toBeInsertedPhase.getKanbanId())) {
            if (!kanbanDsGateway.isAdmin(userId, toBeInsertedPhase.getKanbanId())) {
                throw new UserNotAuthorizedException("User with id: " + userId + " is not the admin of the kanban with id: " + toBeInsertedPhase.getKanbanId());
            }
        }
        var lastPhase = kanbanDsGateway.findLastPhase(toBeInsertedPhase.getKanbanId());
        if (Objects.equals(lastPhase.getId(), toBeInsertedPhase.getId())) {
            return;
        }

        Optional<String> optionalNewRank = RankUtils.calculateMiddleRank(
                getRank(lastPhase.getRank()),
                BaseConversionUtil.MAX_VALUE
        );
        if (optionalNewRank.isEmpty()) {
            reIndex(toBeInsertedPhase.getKanbanId());
            // find after reindexing
            lastPhase = kanbanDsGateway.findLastPhase(toBeInsertedPhase.getKanbanId());
        }

        // this time is for sure good
        String actualNewRank = RankUtils.calculateMiddleRank(
                getRank(lastPhase.getRank()),
                BaseConversionUtil.MAX_VALUE
        ).get();

        String newRank = String.valueOf(getBucket(lastPhase.getRank())) + '|' + actualNewRank;

        kanbanDsGateway.updatePhase(
                toBeInsertedPhase.getId(),
                toBeInsertedPhase.getTitle(),
                newRank
        );
    }

    @Override
    @Transactional
    public void addFirstPhase(Long userId, MovePhaseRequestModel phase) {
        var toBeInsertedPhase = kanbanDsGateway.findPhaseById(phase.getToBeInserted());
        if (toBeInsertedPhase == null) {
            throw new PhaseNotFoundException("Phase not found");
        }
        if (!kanbanDsGateway.isOwner(userId, toBeInsertedPhase.getKanbanId())) {
            if (!kanbanDsGateway.isAdmin(userId, toBeInsertedPhase.getKanbanId())) {
                throw new UserNotAuthorizedException("User with id: " + userId + " is not the admin of the kanban with id: " + toBeInsertedPhase.getKanbanId());
            }
        }
        var firstPhase = kanbanDsGateway.findFirstPhase(toBeInsertedPhase.getKanbanId());
        if (Objects.equals(firstPhase.getId(), toBeInsertedPhase.getId())) {
            return;
        }
        var secondPhase = kanbanDsGateway.findNthPhase(toBeInsertedPhase.getKanbanId(), 1);
        if (secondPhase == null) {
            return;
        }
        kanbanDsGateway.updatePhase(
                toBeInsertedPhase.getId(),
                toBeInsertedPhase.getTitle(),
                firstPhase.getRank()
        );
        Optional<String> newFirstPhaseRankOptional = RankUtils.calculateMiddleRank(
                getRank(firstPhase.getRank()),
                getRank(secondPhase.getRank())
        );
        if (newFirstPhaseRankOptional.isEmpty()) {
            reIndex(toBeInsertedPhase.getKanbanId());
        }

        String newFirstPhaseRank = RankUtils.calculateMiddleRank(
                getRank(firstPhase.getRank()),
                getRank(secondPhase.getRank())
        ).get();

        String firstPhaseCompleteRank = String.valueOf(getBucket(secondPhase.getRank())) + '|' + newFirstPhaseRank;
        kanbanDsGateway.updatePhase(
                firstPhase.getId(),
                firstPhase.getTitle(),
                firstPhaseCompleteRank
        );
    }

    @Override
    public List<PhaseResponseModel> findAllPhasesInOrder(Long userId, Long kanbanId) {
        if (!kanbanDsGateway.isOwner(userId, kanbanId)) {
            if (!kanbanDsGateway.isAdmin(userId, kanbanId)) {
                if (!kanbanDsGateway.isParticipant(userId, kanbanId)) {
                    throw new UserNotAuthorizedException("User with id: " + userId + " is not the admin of the kanban with id: " + kanbanId);
                }
            }
        }
        List<PhaseDsResponseModel> phases = kanbanDsGateway.findAllPhasesForKanbanOrdered(kanbanId, OrderingType.ASCENDING);
        return phases.stream().map(p -> new PhaseResponseModel(
                p.getId(),
                p.getRank(),
                p.getTitle()
        )).toList();
    }

    @Override
    public IssueResponseModel createIssue(Long userId, CreateIssueRequestModel issueRequestModel) {
        var phase = kanbanDsGateway.findPhaseById(issueRequestModel.getPhaseId());
        if (phase == null) {
            throw new PhaseNotFoundException("Phase not found");
        }
        if (!kanbanDsGateway.isOwner(userId, phase.getKanbanId())) {
            if (!kanbanDsGateway.isAdmin(userId, phase.getKanbanId())) {
                if (!kanbanDsGateway.isParticipant(userId, phase.getKanbanId())) {
                    throw new UserNotAuthorizedException("User with id: " + userId + " is not the admin of the kanban with id: " + phase.getKanbanId());
                }
            }
        }
        var createdIssue = kanbanDsGateway.createIssue(new CreateIssueDsRequestModel(
                issueRequestModel.getTitle(),
                issueRequestModel.getDescription(),
                issueRequestModel.getPriority(),
                new Date(),
                issueRequestModel.getExpectedDeadline(),
                issueRequestModel.getPhaseId()
        ));
        return new IssueResponseModel(
                createdIssue.getId(),
                createdIssue.getTitle(),
                createdIssue.getDescription(),
                createdIssue.getPriority(),
                createdIssue.getCreationTimestamp(),
                createdIssue.getExpectedDeadline(),
                createdIssue.getPhaseId()
        );
    }

    private String getRank(String value) {
        String[] tokens = value.split("\\|");
        return tokens[1];
    }

    private int getBucket(String rank) {
        String[] tokens = rank.split("\\|");
        return Integer.parseInt(tokens[0]);
    }

    private int getNextBucket(int currentBucket) {
        if (currentBucket >= 2) {
            return 0;
        }
        return currentBucket + 1;
    }
}
