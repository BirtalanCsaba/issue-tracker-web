package com.issue.tracker.infra.persistence.kanban;

import com.issue.tracker.infra.persistence.user.BaseRepositoryProvider;
import jakarta.ejb.Stateless;
import jakarta.persistence.NoResultException;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaDelete;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

@Stateless
public class KanbanRepositoryImpl extends BaseRepositoryProvider<KanbanEntity, Long> implements KanbanRepository {
    @Override
    public void removeById(Long entityId) {
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
}
