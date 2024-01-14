package com.issue.tracker.infra.persistence.kanban;

import com.issue.tracker.api.logger.LogType;
import com.issue.tracker.api.logger.LoggerBuilder;
import com.issue.tracker.api.persistence.auth.UserDsResponseModel;
import com.issue.tracker.api.persistence.common.OrderingType;
import com.issue.tracker.api.persistence.kanban.*;
import com.issue.tracker.infra.logger.LoggerBuilderImpl;
import com.issue.tracker.infra.persistence.user.UserEntity;
import com.issue.tracker.infra.persistence.user.UserRepository;
import jakarta.annotation.Resource;
import jakarta.ejb.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import jakarta.transaction.UserTransaction;

import java.util.*;

@Stateless
@TransactionManagement(TransactionManagementType.BEAN)
public class KanbanDsGatewayImpl implements KanbanDsGateway {
    @PersistenceContext(unitName = "jpa")
    private EntityManager em;

    @Resource
    private SessionContext ctx;

    @EJB
    private KanbanRepository kanbanRepository;

    @EJB
    private UserRepository userRepository;

    @EJB
    private PhaseRepository phaseRepository;

    @EJB
    private IssueRepository issueRepository;

    @Override
    @Transactional
    public KanbanDsResponseModel create(CreateKanbanDsRequestModel kanban) {
        UserEntity owner = userRepository.findById(kanban.getOwnerId());
        KanbanEntity kanbanEntity = kanbanRepository.save(
                new KanbanEntity(
                        kanban.getTitle(),
                        kanban.getDescription(),
                        owner
                )
        );

        List<UserEntity> participantUsers = userRepository.findAllUsersWithIds(kanban.getParticipants());
        List<UserEntity> adminUsers = userRepository.findAllUsersWithIds(kanban.getAdmins());

        Set<KanbanUserEntity> kanbanUsers = getKanbanUserEntities(participantUsers, adminUsers, kanbanEntity);

        if (kanbanUsers.size() != kanban.getParticipants().size() + kanban.getAdmins().size()) { // + 1 for the owner
            throw new SomeUsersNotFoundException("Some users where not found");
        }

        for (var participant : kanbanUsers) {
            em.persist(participant);
        }
        em.flush();

        return new KanbanDsResponseModel(
                kanbanEntity.getId(),
                kanbanEntity.getTitle(),
                kanbanEntity.getDescription(),
                owner.getId(),
                kanban.getAdmins(),
                kanban.getParticipants()
        );
    }

    private Set<KanbanUserEntity> getKanbanUserEntities(List<UserEntity> participants, List<UserEntity> admins, KanbanEntity kanban) {
        Set<KanbanUserEntity> kanbanUsers = new HashSet<>();
        for (var participant : participants) {
            KanbanUserEntity currentKanbanParticipantEntityAssociation = new KanbanUserEntity();
            currentKanbanParticipantEntityAssociation.setUser(participant);
            currentKanbanParticipantEntityAssociation.setKanban(kanban);
            currentKanbanParticipantEntityAssociation.setRole(KanbanUserRole.PARTICIPANT);
            currentKanbanParticipantEntityAssociation.setId(new KanbanUserPK(participant.getId(), kanban.getId()));
            kanbanUsers.add(currentKanbanParticipantEntityAssociation);
        }
        for (var admin : admins) {
            KanbanUserEntity currentKanbanParticipantEntityAssociation = new KanbanUserEntity();
            currentKanbanParticipantEntityAssociation.setUser(admin);
            currentKanbanParticipantEntityAssociation.setKanban(kanban);
            currentKanbanParticipantEntityAssociation.setRole(KanbanUserRole.ADMIN);
            currentKanbanParticipantEntityAssociation.setId(new KanbanUserPK(admin.getId(), kanban.getId()));
            kanbanUsers.add(currentKanbanParticipantEntityAssociation);
        }
        return kanbanUsers;
    }

    @Override
    @Transactional
    public KanbanDsResponseModel findById(Long id) {
        KanbanEntity result = kanbanRepository.findById(id);

        return new KanbanDsResponseModel(
                result.getId(),
                result.getTitle(),
                result.getDescription(),
                result.getOwner().getId(),
                result.getAdminIds(),
                result.getParticipantsIds()
        );
    }

    @Override
    @Transactional
    public KanbanDsCompleteResponseModel findCompleteById(Long id) {
        KanbanEntity kanban = kanbanRepository.findById(id);
        return new KanbanDsCompleteResponseModel(
                kanban.getId(),
                kanban.getTitle(),
                kanban.getDescription(),
                kanban.getAdmins().stream().map(p -> new UserDsResponseModel(
                        p.getId(),
                        p.getFirstName(),
                        p.getLastName(),
                        p.getUsername(),
                        p.getEmail()
                )).toList(),
                kanban.getParticipants().stream().map(p -> new UserDsResponseModel(
                        p.getId(),
                        p.getFirstName(),
                        p.getLastName(),
                        p.getUsername(),
                        p.getEmail()
                )).toList(),
                new UserDsResponseModel(
                        kanban.getOwner().getId(),
                        kanban.getOwner().getFirstName(),
                        kanban.getOwner().getLastName(),
                        kanban.getOwner().getUsername(),
                        kanban.getOwner().getEmail()
                ),
                kanban.getPhase().stream().map(p -> new CompletePhaseDsResponseModel(
                        p.getId(),
                        p.getRank(),
                        p.getTitle(),
                        p.getKanban().getId(),
                        p.getIssues().stream().map(i -> new IssueDsResponseModel(
                                i.getId(),
                                i.getTitle(),
                                i.getDescription(),
                                i.getPriority(),
                                i.getCreationTimestamp(),
                                i.getExpectedDeadline(),
                                i.getPhase().getId(),
                                i.getAssignedUser().getId()
                        )).toList()
                )).toList()
        );
    }

    @Override
    @Transactional
    public KanbanDsResponseModel findByTitle(String title) {
        KanbanEntity result = kanbanRepository.findByTitle(title);

        return new KanbanDsResponseModel(
                result.getId(),
                result.getTitle(),
                result.getDescription(),
                result.getOwner().getId(),
                result.getAdminIds(),
                result.getParticipantsIds()
        );
    }

    @Override
    @Transactional
    public void update(UpdateKanbanDsRequestModel kanban) {
        kanbanRepository.update(new KanbanEntity(
                kanban.getId(),
                kanban.getTitle(),
                kanban.getDescription()
        ));
    }

    @Override
    @Transactional
    public List<EnrolledKanbanDsResponseModel> findAllByUserId(Long userId) {
        List<EnrolledKanbanDsResponseModel> result = new ArrayList<>();

        // Retrieve enrollments
        List<KanbanUserEntity> enrollments = kanbanRepository.findEnrollments(userId);
        for (var enrollment : enrollments) {
            result.add(new EnrolledKanbanDsResponseModel(
                    enrollment.getKanban().getId(),
                    enrollment.getKanban().getTitle(),
                    enrollment.getKanban().getDescription(),
                    enrollment.getKanban().getOwner().getId(),
                    enrollment.getKanban().getAdminIds(),
                    enrollment.getKanban().getParticipantsIds(),
                    enrollment.convertUserRoleToString()
            ));
        }

        List<KanbanEntity> whereOwner = kanbanRepository.findOwningKanbans(userId);
        for (var kanban : whereOwner) {
            result.add(new EnrolledKanbanDsResponseModel(
                    kanban.getId(),
                    kanban.getTitle(),
                    kanban.getDescription(),
                    userId,
                    kanban.getAdminIds(),
                    kanban.getParticipantsIds(),
                    "OWNER"
            ));
        }

        return result;
    }

    @Override
    @Transactional
    public boolean isOwner(Long userId, Long kanbanId) {
        return kanbanRepository.isOwner(userId, kanbanId);
    }

    @Override
    @Transactional
    public boolean isAdmin(Long userId, Long kanbanId) {
        return kanbanRepository.isAdmin(userId, kanbanId);
    }

    @Override
    @Transactional
    public boolean isParticipant(Long userId, Long kanbanId) {
        return kanbanRepository.isParticipant(userId, kanbanId);
    }

    @Override
    @Transactional
    public void removeById(Long id) {
        kanbanRepository.removeById(id);
    }

    @Override
    @Transactional
    public long getPhaseCount(Long kanbanId) {
        return kanbanRepository.getPhaseCount(kanbanId);
    }

    @Override
    @Transactional
    public void updatePhase(Long phaseId, String title, String rank) {
        String jpqlUpdate = "UPDATE PhaseEntity e SET e.title = :title, e.rank = :rank WHERE e.id = :id";

        Query query = em.createQuery(jpqlUpdate)
                .setParameter("id", phaseId)
                .setParameter("title", title)
                .setParameter("rank", rank);

        int updatedEntities = query.executeUpdate();
    }

    @Override
    public PhaseDsResponseModel findNthPhase(Long kanbanId, int index) {
        var response = phaseRepository.getNthElemAfterSort(kanbanId, index);
        if (response == null) {
            return null;
        }
        return new PhaseDsResponseModel(
                response.getId(),
                response.getRank(),
                response.getTitle(),
                response.getKanban().getId()
        );
    }

    @Override
    @Transactional
    public PhaseDsResponseModel findFirstPhase(Long kanbanId) {
        PhaseEntity result = phaseRepository.findFirstPhaseForKanban(kanbanId);
        if (result == null) {
            return null;
        }
        return new PhaseDsResponseModel(
                result.getId(),
                result.getRank(),
                result.getTitle(),
                result.getKanban().getId()
        );
    }

    @Override
    @Transactional
    public PhaseDsResponseModel findLastPhase(Long kanbanId) {
        PhaseEntity result = phaseRepository.findLastPhaseForKanban(kanbanId);
        if (result == null) {
            return null;
        }
        return new PhaseDsResponseModel(
                result.getId(),
                result.getRank(),
                result.getTitle(),
                result.getKanban().getId()
        );
    }

    @Override
    @Transactional
    public List<PhaseDsResponseModel> findAllPhasesForKanban(Long kanbanId) {
        var result = findAllPhasesForKanbanOrdered(kanbanId, OrderingType.ASCENDING);
        return result.stream().map(p -> new PhaseDsResponseModel(
                p.getId(),
                p.getRank(),
                p.getTitle(),
                p.getKanbanId()
        )).toList();
    }

    @Override
    @Transactional
    public List<PhaseDsResponseModel> findAllPhasesForKanbanOrdered(Long kanbanId, OrderingType order) {
        List<PhaseEntity> result = phaseRepository.findAllPhasesForKanbanOrdered(kanbanId, order);
        return result.stream().map(p -> new PhaseDsResponseModel(
                p.getId(),
                p.getRank(),
                p.getTitle(),
                p.getKanban().getId()
        )).toList();
    }

    @Override
    @Transactional
    public PhaseDsResponseModel addPhase(CreatePhaseRequestModel phase) {
        KanbanEntity kanban = kanbanRepository.findById(phase.getKanbanId());
        PhaseEntity createdPhase = phaseRepository.save(new PhaseEntity(
                phase.getRank(),
                kanban,
                phase.getTitle()
        ));
        return new PhaseDsResponseModel(
                createdPhase.getId(),
                createdPhase.getRank(),
                createdPhase.getTitle(),
                createdPhase.getKanban().getId()
        );
    }

    @Override
    public void updatePhases(List<UpdatePhaseRequestModel> phases) {
        UserTransaction transaction = ctx.getUserTransaction();
        try {
            transaction.begin();

            String query = "update PhaseEntity p set p.title=:title, p.rank=:rank where p.id=:phaseId";

            long count = 0;
            for (var phase : phases) {
                if (count >= 100) {
                    count = 0;
                    transaction.commit();
                }
                Query theQuery = em.createQuery(query);
                theQuery.setParameter("title", phase.getTitle());
                theQuery.setParameter("rank", phase.getRank());
                theQuery.setParameter("phaseId", phase.getId());
                theQuery.executeUpdate();
                count++;
            }
            transaction.commit();
        } catch (Exception e) {
            try {
                transaction.rollback();
            } catch (Exception rollbackException) {
                // Handle rollback exception if necessary
                LoggerBuilderImpl loggerBuilder = new LoggerBuilderImpl();
                loggerBuilder.create(getClass(), LogType.ERROR, rollbackException.getMessage())
                        .build()
                        .print();
            }

            // Handle the original exception
            LoggerBuilderImpl loggerBuilder = new LoggerBuilderImpl();
            loggerBuilder.create(getClass(), LogType.ERROR, e.getMessage())
                    .build()
                    .print();
        }
    }

    @Override
    public PhaseDsResponseModel findPhaseById(Long phaseId) {
        PhaseEntity phase = phaseRepository.findById(phaseId);
        return new PhaseDsResponseModel(
                phase.getId(),
                phase.getRank(),
                phase.getTitle(),
                phase.getKanban().getId()
        );
    }

    @Override
    @Transactional
    public IssueDsResponseModel createIssue(CreateIssueDsRequestModel issueDsRequestModel) {
        PhaseEntity thePhase = phaseRepository.findById(issueDsRequestModel.getPhaseId());
        if (thePhase == null) {
            return null;
        }
        UserEntity assignedUser = userRepository.findById(issueDsRequestModel.getAssignedUser());
        IssueEntity createdIssue = issueRepository.save(new IssueEntity(
                issueDsRequestModel.getTitle(),
                issueDsRequestModel.getDescription(),
                issueDsRequestModel.getPriority(),
                issueDsRequestModel.getExpectedDeadline(),
                thePhase,
                assignedUser
        ));
        return new IssueDsResponseModel(
                createdIssue.getId(),
                createdIssue.getTitle(),
                createdIssue.getDescription(),
                createdIssue.getPriority(),
                createdIssue.getCreationTimestamp(),
                createdIssue.getExpectedDeadline(),
                createdIssue.getPhase().getId(),
                createdIssue.getAssignedUser().getId()
        );
    }

    @Override
    @Transactional
    public void deleteIssue(Long issueId) {
        issueRepository.removeById(issueId);
    }

    @Override
    @Transactional
    public IssueDsResponseModel findIssueById(Long issueId) {
        var response = issueRepository.findById(issueId);
        return new IssueDsResponseModel(
                response.getId(),
                response.getTitle(),
                response.getDescription(),
                response.getPriority(),
                response.getCreationTimestamp(),
                response.getExpectedDeadline(),
                response.getPhase().getId(),
                response.getAssignedUser().getId()
        );
    }

    @Override
    @Transactional
    public void deletePhase(Long phaseId) {
        phaseRepository.removeById(phaseId);
    }
}
