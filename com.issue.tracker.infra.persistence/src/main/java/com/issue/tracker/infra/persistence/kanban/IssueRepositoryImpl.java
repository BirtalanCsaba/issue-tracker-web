package com.issue.tracker.infra.persistence.kanban;

import com.issue.tracker.infra.persistence.user.BaseRepositoryProvider;
import jakarta.ejb.Stateless;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;

@Stateless
public class IssueRepositoryImpl extends BaseRepositoryProvider<IssueEntity, Long>  implements IssueRepository {

    @Override
    public void removeById(Long entityId) {
        String query = "delete from IssueEntity i where i.id=:issueId";
        Query theQuery = em.createQuery(query);
        theQuery.setParameter("issueId", entityId);
        theQuery.executeUpdate();
    }

    @Override
    public IssueEntity findById(Long entityId) {
        try {
            String query = "select i from IssueEntity i where i.id=:issueId";
            TypedQuery<IssueEntity> theQuery = em.createQuery(query, IssueEntity.class);
            theQuery.setParameter("issueId", entityId);
            return theQuery.getSingleResult();
        } catch (NoResultException ex) {
            return null;
        }

    }
}
