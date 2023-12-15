package com.issue.tracker.infra.persistence.kanban;

import com.issue.tracker.api.persistence.kanban.CreateKanbanDsRequestModel;
import com.issue.tracker.api.persistence.kanban.KanbanDsGateway;
import com.issue.tracker.api.persistence.kanban.KanbanDsResponseModel;
import com.issue.tracker.api.persistence.kanban.UpdateKanbanDsRequestModel;
import com.issue.tracker.infra.persistence.user.UserEntity;
import com.issue.tracker.infra.persistence.user.UserEntity_;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.*;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Stateless
public class KanbanDsGatewayImpl implements KanbanDsGateway {
    @PersistenceContext(unitName = "jpa")
    private EntityManager em;

    @Override
    public KanbanDsResponseModel create(CreateKanbanDsRequestModel kanban) {
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();

            KanbanEntity kanbanEntity = new KanbanEntity(kanban.getTitle(), kanban.getDescription());
            em.persist(kanbanEntity);
            em.flush();

            Set<KanbanUserEntity> kanbanUsers = getKanbanUserEntities(kanban, kanbanEntity);

            for (var participant : kanbanUsers) {
                em.persist(participant);
            }
            em.flush();

            transaction.commit();

            return new KanbanDsResponseModel(
                    kanbanEntity.getId(),
                    kanbanEntity.getTitle(),
                    kanbanEntity.getDescription(),
                    kanbanEntity.getOwnerIds(),
                    kanban.getParticipants()
            );
        } catch (RuntimeException ex) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw ex;
        }
    }

    @NotNull
    private static Set<KanbanUserEntity> getKanbanUserEntities(CreateKanbanDsRequestModel kanban, KanbanEntity kanbanEntity) {
        Set<KanbanUserEntity> kanbanUsers = new HashSet<>();
        KanbanUserEntity owner = new KanbanUserEntity(
                new KanbanUserPK(kanban.getOwnerId(), kanbanEntity.getId())
        );
        kanbanUsers.add(owner);
        for (var participant : kanban.getParticipants()) {
            KanbanUserEntity currentKanbanParticipantEntity = new KanbanUserEntity(
                    new KanbanUserPK(participant, kanbanEntity.getId())
            );
            kanbanUsers.add(currentKanbanParticipantEntity);
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
