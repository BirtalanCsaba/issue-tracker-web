package com.issue.tracker.api.persistence.kanban;

public interface IssueDsGateway {
    IssueDsResponseModel create(CreateIssueDsRequestModel issue);

    IssueDsResponseModel findById(Long id);

    void removeById(Long id);

    IssueDsResponseModel update(UpdateIssueDsRequestModel issue);
}
