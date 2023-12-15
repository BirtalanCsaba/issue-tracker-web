package com.issue.tracker.infra.persistence.user;

import com.issue.tracker.infra.persistence.common.BaseEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.io.Serializable;

public abstract class BaseRepositoryProvider <ENTITY extends BaseEntity<ID>, ID extends Serializable> implements BaseRepository<ENTITY, ID>{
    @PersistenceContext(unitName = "jpa")
    protected EntityManager em;

    @Override
    public ENTITY save(ENTITY entity) {
        em.persist(entity);
        em.flush();
        return entity;
    }

    @Override
    public void update(ENTITY entity) {
        em.merge(entity);
        em.flush();
    }
}
