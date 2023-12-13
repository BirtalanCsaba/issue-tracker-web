package com.issue.tracker.infra.persistence.kanban;

import com.issue.tracker.api.persistence.kanban.CreateIssueDsRequestModel;
import com.issue.tracker.api.persistence.kanban.IssueDsGateway;
import com.issue.tracker.api.persistence.kanban.IssueDsResponseModel;
import com.issue.tracker.api.persistence.kanban.UpdateIssueDsRequestModel;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Stateless
public class IssueDsGatewayImpl implements IssueDsGateway {
    @PersistenceContext(unitName = "jpa")
    private EntityManager em;

    @Override
    public IssueDsResponseModel create(CreateIssueDsRequestModel issue) {
        return null;
    }

    @Override
    public IssueDsResponseModel findById(Long id) {
        return null;
    }

    @Override
    public void removeById(Long id) {

    }

    @Override
    public IssueDsResponseModel update(UpdateIssueDsRequestModel issue) {
        return null;
    }
}
