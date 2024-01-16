package com.issue.tracker.infra.persistence.kanban;

import com.issue.tracker.api.persistence.common.OrderingType;
import com.issue.tracker.infra.persistence.user.BaseRepository;
import com.issue.tracker.infra.persistence.user.BaseRepositoryProvider;
import jakarta.ejb.Stateless;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaDelete;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

import java.util.List;

@Stateless
public class PhaseRepositoryImpl extends BaseRepositoryProvider<PhaseEntity, Long> implements PhaseRepository {

    @Override
    public void removeById(Long entityId) {
        String query = "delete from IssueEntity i where i.phase.id=:phaseId";
        Query theQuery = em.createQuery(query);
        theQuery.setParameter("phaseId", entityId);
        theQuery.executeUpdate();

        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaDelete<PhaseEntity> delete = criteriaBuilder.createCriteriaDelete(PhaseEntity.class);
        Root<PhaseEntity> root = delete.from(PhaseEntity.class);
        delete.where(criteriaBuilder.equal(root.get(PhaseEntity_.ID), entityId));
        em.createQuery(delete).executeUpdate();
    }

    @Override
    public PhaseEntity findById(Long entityId) {
        try {
            CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
            CriteriaQuery<PhaseEntity> query = criteriaBuilder.createQuery(PhaseEntity.class);
            Root<PhaseEntity> root = query.from(PhaseEntity.class);
            query.where(criteriaBuilder.equal(root.get(PhaseEntity_.ID), entityId));
            return em.createQuery(query).getSingleResult();
        } catch (NoResultException ex) {
            return null;
        }
    }

    @Override
    public PhaseEntity findLastPhaseForKanban(Long kanbanId) {
        try {
            TypedQuery<PhaseEntity> query = em.createQuery(
                    "SELECT e FROM PhaseEntity e where e.kanban.id=:kanbanId ORDER BY e.rank DESC", PhaseEntity.class);

            query.setParameter("kanbanId", kanbanId);
            query.setMaxResults(1);

            return query.getSingleResult();
        } catch (NoResultException ex) {
            return null;
        }
    }

    @Override
    public PhaseEntity findFirstPhaseForKanban(Long kanbanId) {
        try {
            TypedQuery<PhaseEntity> query = em.createQuery(
                    "SELECT e FROM PhaseEntity e where e.kanban.id=:kanbanId ORDER BY e.rank ASC", PhaseEntity.class);

            query.setParameter("kanbanId", kanbanId);
            query.setMaxResults(1);

            return query.getSingleResult();
        } catch (NoResultException ex) {
            return null;
        }
    }

    @Override
    public PhaseEntity getNthElemAfterSort(Long kanbanId, long index) {
        String jpqlQuery = "SELECT e FROM PhaseEntity e where e.kanban.id=:kanbanId ORDER BY e.rank";

        // Create a query and set the ordering and limits
        TypedQuery<PhaseEntity> query = em.createQuery(jpqlQuery, PhaseEntity.class)
                .setParameter("kanbanId", kanbanId)
                .setFirstResult((int) index)  // Adjusting for 0-based indexing
                .setMaxResults(1);

        // Execute the query and retrieve the result
        List<PhaseEntity> resultList = query.getResultList();

        // Check if there is a result
        if (resultList != null && !resultList.isEmpty()) {
            return resultList.get(0);
        } else {
            return null; // No result found
        }
    }

    @Override
    public List<PhaseEntity> findAllPhasesForKanbanOrdered(Long kanbanId, OrderingType order) {
        String jpqlQuery;
        if (order == OrderingType.ASCENDING) {
            jpqlQuery = "SELECT e FROM PhaseEntity e where e.kanban.id = :kanbanId ORDER BY e.rank ASC";
        } else {
            jpqlQuery = "SELECT e FROM PhaseEntity e where e.kanban.id = :kanbanId ORDER BY e.rank DESC";
        }

        TypedQuery<PhaseEntity> query = em.createQuery(jpqlQuery, PhaseEntity.class);
        query.setParameter("kanbanId", kanbanId);

        return query.getResultList();
    }
}
