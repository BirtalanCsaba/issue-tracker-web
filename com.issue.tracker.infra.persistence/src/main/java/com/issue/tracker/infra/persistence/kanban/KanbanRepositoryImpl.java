package com.issue.tracker.infra.persistence.kanban;

import com.issue.tracker.infra.persistence.user.BaseRepositoryProvider;
import com.issue.tracker.infra.persistence.user.UserEntity;
import com.issue.tracker.infra.persistence.user.UserEntity_;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityGraph;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Stateless
public class KanbanRepositoryImpl extends BaseRepositoryProvider<KanbanEntity, Long> implements KanbanRepository {
    @Override
    public void removeById(Long entityId) {
        String query = "delete from KanbanUserEntity ku where ku.id.kanbanId = :kanbanId";
        Query theQuery = em.createQuery(query);
        theQuery.setParameter("kanbanId", entityId);
        theQuery.executeUpdate();

        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaDelete<KanbanEntity> delete = criteriaBuilder.createCriteriaDelete(KanbanEntity.class);
        Root<KanbanEntity> root = delete.from(KanbanEntity.class);
        delete.where(criteriaBuilder.equal(root.get(KanbanEntity_.ID), entityId));
        em.createQuery(delete).executeUpdate();
    }

    @Override
    public KanbanEntity findById(Long entityId) {
        try {
            CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
            CriteriaQuery<KanbanEntity> query = criteriaBuilder.createQuery(KanbanEntity.class);
            Root<KanbanEntity> root = query.from(KanbanEntity.class);
            query.where(criteriaBuilder.equal(root.get(KanbanEntity_.ID), entityId));
            return em.createQuery(query).getSingleResult();
        } catch (NoResultException ex) {
            return null;
        }
    }

    @Override
    public List<KanbanEntity> findOwningKanbans(Long userId) {
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<KanbanEntity> criteriaQuery = criteriaBuilder.createQuery(KanbanEntity.class);
        Root<KanbanEntity> root = criteriaQuery.from(KanbanEntity.class);
        Join<KanbanEntity, UserEntity> kanbanUserJoin = root.join(KanbanEntity_.OWNER);
        criteriaQuery.where(criteriaBuilder.equal(kanbanUserJoin.get(UserEntity_.ID), userId));
        criteriaQuery.distinct(true);
        return em.createQuery(criteriaQuery).getResultList();
    }

    @Override
    public List<KanbanUserEntity> findEnrollments(Long userId) {
        CriteriaBuilder ownerCriteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<KanbanUserEntity> criteriaQuery = ownerCriteriaBuilder.createQuery(KanbanUserEntity.class);
        Root<KanbanUserEntity> ownerRoot = criteriaQuery.from(KanbanUserEntity.class);
        Join<KanbanUserEntity, UserEntity> kanbanUserJoin = ownerRoot.join(KanbanUserEntity_.USER);
        criteriaQuery.where(ownerCriteriaBuilder.equal(kanbanUserJoin.get(UserEntity_.id), userId));
        criteriaQuery.distinct(true);
        return em.createQuery(criteriaQuery).getResultList();
    }

    @Override
    public KanbanEntity findByTitle(String title) {
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<KanbanEntity> criteriaQuery = criteriaBuilder.createQuery(KanbanEntity.class);
        Root<KanbanEntity> root = criteriaQuery.from(KanbanEntity.class);
        criteriaQuery.where(criteriaBuilder.equal(root.get(KanbanEntity_.TITLE), title));
        return em.createQuery(criteriaQuery).getSingleResult();
    }

    @Override
    public boolean isOwner(Long userId, Long kanbanId) {
        String query = "select count(k) > 0 from KanbanEntity k where k.owner.id=:userId and k.id=:kanbanId";
        TypedQuery<Boolean> typedQuery = em.createQuery(query, Boolean.class);
        typedQuery.setParameter("userId", userId);
        typedQuery.setParameter("kanbanId", kanbanId);
        return typedQuery.getSingleResult();
    }

    @Override
    public boolean isAdmin(Long userId, Long kanbanId) {
        String query = "select ku from KanbanUserEntity ku where ku.id.kanbanId=:kanbanId and ku.id.userId=:userId and ku.role = 'ADMIN'";
        TypedQuery<Boolean> typedQuery = em.createQuery(query, Boolean.class);
        typedQuery.setParameter("userId", userId);
        typedQuery.setParameter("kanbanId", kanbanId);
        return typedQuery.getSingleResult();
    }

    @Override
    public boolean isParticipant(Long userId, Long kanbanId) {
        String query = "select ku from KanbanUserEntity ku where ku.id.kanbanId=:kanbanId and ku.id.userId=:userId and ku.role = 'PARTICIPANT'";
        TypedQuery<Boolean> typedQuery = em.createQuery(query, Boolean.class);
        typedQuery.setParameter("userId", userId);
        typedQuery.setParameter("kanbanId", kanbanId);
        return typedQuery.getSingleResult();
    }

    @Override
    public KanbanEntity findByIdWithUsers(Long kanbanId) {
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<KanbanEntity> criteriaQuery = criteriaBuilder.createQuery(KanbanEntity.class);
        Root<KanbanEntity> root = criteriaQuery.from(KanbanEntity.class);
        criteriaQuery.where(criteriaBuilder.equal(root.get(KanbanEntity_.ID), kanbanId));
        EntityGraph<?> entityGraph = em.getEntityGraph("user-participant");
        TypedQuery<KanbanEntity> query = em.createQuery(criteriaQuery);
        query.setHint("jakarta.persistence.loadgraph", entityGraph);
        return em.createQuery(criteriaQuery).getSingleResult();
    }
}
