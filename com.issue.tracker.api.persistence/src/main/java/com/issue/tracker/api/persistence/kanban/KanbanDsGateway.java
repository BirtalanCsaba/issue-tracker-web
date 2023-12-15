package com.issue.tracker.api.persistence.kanban;

import jakarta.ejb.Remote;

import java.util.List;

@Remote
public interface KanbanDsGateway {
    KanbanDsResponseModel create(CreateKanbanDsRequestModel kanban);

    KanbanDsResponseModel findById(Long id);

    KanbanDsResponseModel findByTitle(String title);

    KanbanDsResponseModel update(UpdateKanbanDsRequestModel kanban);

    List<KanbanDsResponseModel> findAllByUserId(Long userId);

    void removeById(Long id);
}
