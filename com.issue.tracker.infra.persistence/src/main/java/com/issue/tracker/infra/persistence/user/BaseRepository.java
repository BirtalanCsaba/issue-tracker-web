package com.issue.tracker.infra.persistence.user;

import com.issue.tracker.infra.persistence.common.BaseEntity;

import java.io.Serializable;

public interface BaseRepository <ENTITY extends BaseEntity<ID>, ID extends Serializable> {

    /**
     * Persists the given entity to the database
     * @param entity The entity to be saved
     * @return The created entity with its new ID
     */
    ENTITY save(ENTITY entity);

    /**
     * Update the current entity in the database
     * @param entity The entity to be updated
     */
    void update(ENTITY entity);

    /**
     * Remove an entity by id
     * @param entityId The entity id
     */
    void removeById(ID entityId);

    /**
     * Find an entity with a given ID
     * @param entityId The entity ID
     * @return The found entity
     */
    ENTITY findById(ID entityId);
}
