package com.issue.tracker.api.kanban;

import com.issue.tracker.api.ApiException;
import com.issue.tracker.api.auth.UserNotAuthorizedException;
import com.issue.tracker.api.common.BaseConversionUtil;
import com.issue.tracker.api.persistence.common.OrderingType;
import com.issue.tracker.api.persistence.kanban.*;
import com.issue.tracker.domain.kanban.BaseKanbanFactory;
import com.issue.tracker.domain.kanban.Kanban;
import com.issue.tracker.domain.kanban.KanbanFactory;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;

import java.util.List;

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
    public void reIndex(Long kanbanId) {
        PhaseDsResponseModel phase = kanbanDsGateway.findFirstPhase(kanbanId);
        int currentBucket = getBucket(phase.getRank());
        int nextBucket = getNextBucket(currentBucket);

        List<PhaseDsResponseModel> phases;

//        if (currentBucket >= 2) {
        phases = kanbanDsGateway.findAllPhasesForKanbanOrdered(kanbanId, OrderingType.ASCENDING);
//        } else {
//            phases = kanbanDsGateway.findAllPhasesForKanbanOrdered(kanbanId, OrderingType.ASCENDING);
//        }

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
                response.getOwner()
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
