package com.issue.tracker.infra.persistence.kanban;

import com.issue.tracker.api.persistence.auth.AuthDsGateway;
import com.issue.tracker.api.persistence.kanban.*;
import com.issue.tracker.infra.persistence.user.UserEntity;
import com.issue.tracker.infra.persistence.user.UserEntity_;
import com.issue.tracker.infra.persistence.user.UserRepository;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import org.jetbrains.annotations.NotNull;

import java.util.*;

@Stateless
public class KanbanDsGatewayImpl implements KanbanDsGateway {
    @PersistenceContext(unitName = "jpa")
    private EntityManager em;

    @EJB
    private KanbanRepository kanbanRepository;

    @EJB
    private UserRepository userRepository;

    @Override
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
    public void update(UpdateKanbanDsRequestModel kanban) {
        kanbanRepository.update(new KanbanEntity(
                kanban.getId(),
                kanban.getTitle(),
                kanban.getDescription()
        ));
    }

    @Override
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

        // Retrieve where user is owner
        // TODO: create a named entity graph
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
    public boolean isOwner(Long userId, Long kanbanId) {
        return kanbanRepository.isOwner(userId, kanbanId);
    }

    @Override
    public boolean isAdmin(Long userId, Long kanbanId) {
        return kanbanRepository.isAdmin(userId, kanbanId);
    }

    @Override
    public boolean isParticipant(Long userId, Long kanbanId) {
        return kanbanRepository.isAdmin(userId, kanbanId);
    }

    @Override
    public void removeById(Long id) {
        kanbanRepository.removeById(id);
    }
}
