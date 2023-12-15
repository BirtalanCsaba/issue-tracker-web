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
        KanbanEntity kanbanEntity = kanbanRepository.save(
                new KanbanEntity(
                        kanban.getTitle(),
                        kanban.getDescription()
                )
        );
        UserEntity owner = userRepository.findById(kanban.getOwnerId());
        List<UserEntity> users = userRepository.findAllUsersWithIds(kanban.getParticipants());

        Set<KanbanUserEntity> kanbanUsers = getKanbanUserEntities(owner, users, kanbanEntity);

        if (kanbanUsers.size() != kanban.getParticipants().size() + 1) { // + 1 for the owner
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
                Collections.singletonList(owner.getId()),
                kanban.getParticipants()
        );
    }

    private Set<KanbanUserEntity> getKanbanUserEntities(UserEntity owner, List<UserEntity> participants, KanbanEntity kanban) {
        Set<KanbanUserEntity> kanbanUsers = new HashSet<>();
        KanbanUserEntity ownerKanbanAssociation = new KanbanUserEntity();
        ownerKanbanAssociation.setUser(owner);
        ownerKanbanAssociation.setKanban(kanban);
        ownerKanbanAssociation.setRole(KanbanUserRole.OWNER);
        ownerKanbanAssociation.setId(new KanbanUserPK(owner.getId(), kanban.getId()));
        kanbanUsers.add(ownerKanbanAssociation);
        for (var participant : participants) {
            KanbanUserEntity currentKanbanParticipantEntityAssociation = new KanbanUserEntity();
            currentKanbanParticipantEntityAssociation.setUser(participant);
            currentKanbanParticipantEntityAssociation.setKanban(kanban);
            currentKanbanParticipantEntityAssociation.setRole(KanbanUserRole.PARTICIPANT);
            currentKanbanParticipantEntityAssociation.setId(new KanbanUserPK(participant.getId(), kanban.getId()));
            kanbanUsers.add(currentKanbanParticipantEntityAssociation);
        }
        return kanbanUsers;
    }

    @Override
    public KanbanDsResponseModel findById(Long id) {
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<KanbanEntity> criteriaQuery = criteriaBuilder.createQuery(KanbanEntity.class);
        Root<KanbanEntity> root = criteriaQuery.from(KanbanEntity.class);
        criteriaQuery.where(criteriaBuilder.equal(root.get(KanbanEntity_.ID), id));
        KanbanEntity result = em.createQuery(criteriaQuery).getSingleResult();

        return new KanbanDsResponseModel(
                result.getId(),
                result.getTitle(),
                result.getDescription(),
                result.getOwnerIds(),
                result.getParticipantsIds()
        );
    }

    @Override
    public KanbanDsResponseModel findByTitle(String title) {
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<KanbanEntity> criteriaQuery = criteriaBuilder.createQuery(KanbanEntity.class);
        Root<KanbanEntity> root = criteriaQuery.from(KanbanEntity.class);
        criteriaQuery.where(criteriaBuilder.equal(root.get(KanbanEntity_.TITLE), title));
        KanbanEntity result = em.createQuery(criteriaQuery).getSingleResult();

        return new KanbanDsResponseModel(
                result.getId(),
                result.getTitle(),
                result.getDescription(),
                result.getOwnerIds(),
                result.getParticipantsIds()
        );
    }

    @Override
    public KanbanDsResponseModel update(UpdateKanbanDsRequestModel kanban) {
        return null;
    }

    @Override
    public List<KanbanDsResponseModel> findAllByUserId(Long userId) {
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<KanbanEntity> criteriaQuery = criteriaBuilder.createQuery(KanbanEntity.class);
        Root<KanbanEntity> parentRoot = criteriaQuery.from(KanbanEntity.class);

        // KanbanUserEntity join
        Join<KanbanEntity, KanbanUserEntity> kanbanUserJoin = parentRoot.join(KanbanEntity_.USERS);

        // UserEntity join
        Join<KanbanEntity, KanbanUserEntity> userJoin = kanbanUserJoin.join(KanbanUserEntity_.USER);

        // Add a predicate to filter by childProperty
        criteriaQuery.where(criteriaBuilder.equal(userJoin.get(UserEntity_.ID), userId));

        // Remove duplicates caused by the join
        criteriaQuery.distinct(true);

        // Execute the query and retrieve the result
        List<KanbanEntity> result = em.createQuery(criteriaQuery).getResultList();

        return result.stream().map(k -> new KanbanDsResponseModel(
                k.getId(),
                k.getTitle(),
                k.getDescription(),
                k.getOwnerIds(),
                k.getParticipantsIds()
        )).toList();
    }

    @Override
    public void removeById(Long id) {
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaDelete<UserEntity> delete = criteriaBuilder.createCriteriaDelete(UserEntity.class);
        Root<UserEntity> root = delete.from(UserEntity.class);

        // Add a predicate to the delete query to filter by ID
        delete.where(criteriaBuilder.equal(root.get(UserEntity_.ID), id));

        // Execute the delete query
        em.createQuery(delete).executeUpdate();

        em.getTransaction().commit();
    }
}