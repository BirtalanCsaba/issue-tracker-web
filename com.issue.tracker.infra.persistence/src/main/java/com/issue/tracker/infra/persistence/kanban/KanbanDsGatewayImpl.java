package com.issue.tracker.infra.persistence.kanban;

import com.issue.tracker.api.persistence.kanban.CreateKanbanDsRequestModel;
import com.issue.tracker.api.persistence.kanban.KanbanDsGateway;
import com.issue.tracker.api.persistence.kanban.KanbanDsResponseModel;
import com.issue.tracker.api.persistence.kanban.UpdateKanbanDsRequestModel;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.PersistenceContext;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
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
                    kanban.getOwnerId(),
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
        return null;
    }

    @Override
    public KanbanDsResponseModel findByTitle(String title) {
        return null;
    }

    @Override
    public KanbanDsResponseModel update(UpdateKanbanDsRequestModel kanban) {
        return null;
    }

    @Override
    public void removeById(Long id) {

    }
}
