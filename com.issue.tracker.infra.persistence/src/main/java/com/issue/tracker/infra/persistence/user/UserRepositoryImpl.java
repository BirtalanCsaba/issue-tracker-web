package com.issue.tracker.infra.persistence.user;

import jakarta.ejb.Stateless;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;

import java.util.List;

@Stateless
public class UserRepositoryImpl extends BaseRepositoryProvider<UserEntity, Long> implements UserRepository {
    @Override
    public UserEntity findByUsernameAndPassword(String username, String password) {
        try {
            CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
            CriteriaQuery<UserEntity> query = criteriaBuilder.createQuery(UserEntity.class);
            Root<UserEntity> root = query.from(UserEntity.class);
            query.where(criteriaBuilder.equal(root.get(UserEntity_.USERNAME), username));
            query.where(criteriaBuilder.equal(root.get(UserEntity_.PASSWORD), password));
            return em.createQuery(query).getSingleResult();
        } catch (NoResultException ex) {
            return null;
        }
    }

    @Override
    public UserEntity findByUsername(String username) {
        try {
            CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
            CriteriaQuery<UserEntity> query = criteriaBuilder.createQuery(UserEntity.class);
            Root<UserEntity> root = query.from(UserEntity.class);
            query.where(criteriaBuilder.equal(root.get(UserEntity_.USERNAME), username));
            return em.createQuery(query).getSingleResult();
        } catch (NoResultException ex) {
            return null;
        }
    }

    @Override
    public UserEntity findByEmail(String email) {
        try {
            CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
            CriteriaQuery<UserEntity> query = criteriaBuilder.createQuery(UserEntity.class);
            Root<UserEntity> root = query.from(UserEntity.class);
            query.where(criteriaBuilder.equal(root.get(UserEntity_.EMAIL), email));
            return em.createQuery(query).getSingleResult();
        } catch (NoResultException ex) {
            return null;
        }

    }

    @Override
    public List<UserEntity> findAllUsersWithIds(List<Long> userIds) {
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaQuery<UserEntity> query = criteriaBuilder.createQuery(UserEntity.class);
        Root<UserEntity> root = query.from(UserEntity.class);
        Predicate predicate = root.get("id").in(userIds);
        query.where(predicate);
        return em.createQuery(query).getResultList();
    }

    @Override
    public List<UserEntity> findAllOtherUsers(Long exceptUserId) {
        String queryString = "select u from UserEntity u where u.id<>:userId";
        TypedQuery<UserEntity> query = em.createQuery(queryString, UserEntity.class);
        query.setParameter("userId", exceptUserId);
        return query.getResultList();
    }

    @Override
    public void removeById(Long entityId) {
        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        CriteriaDelete<UserEntity> delete = criteriaBuilder.createCriteriaDelete(UserEntity.class);
        Root<UserEntity> root = delete.from(UserEntity.class);
        delete.where(criteriaBuilder.equal(root.get(UserEntity_.ID), entityId));
        em.createQuery(delete).executeUpdate();
    }

    @Override
    public UserEntity findById(Long entityId) {
        try {
            CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
            CriteriaQuery<UserEntity> query = criteriaBuilder.createQuery(UserEntity.class);
            Root<UserEntity> root = query.from(UserEntity.class);
            query.where(criteriaBuilder.equal(root.get(UserEntity_.ID), entityId));
            return em.createQuery(query).getSingleResult();
        } catch (NoResultException ex) {
            return null;
        }
    }
}
