package com.issue.tracker.api.persistence.kanban;

public interface KanbanDsGateway {
    KanbanDsResponseModel create(CreateKanbanDsRequestModel kanban);

    KanbanDsResponseModel findById(Long id);

    KanbanDsResponseModel findByTitle(String title);

    KanbanDsResponseModel update(UpdateKanbanDsRequestModel kanban);

    void removeById(Long id);
}
