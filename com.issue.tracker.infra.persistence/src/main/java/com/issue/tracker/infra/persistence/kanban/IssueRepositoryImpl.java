package com.issue.tracker.infra.persistence.kanban;

import com.issue.tracker.infra.persistence.user.BaseRepositoryProvider;

public class IssueRepositoryImpl extends BaseRepositoryProvider<IssueEntity, Long>  implements IssueRepository {

    @Override
    public void removeById(Long entityId) {
        
    }

    @Override
    public IssueEntity findById(Long entityId) {
        return null;
    }
}
