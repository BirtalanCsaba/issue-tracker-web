package com.issue.tracker.api.kanban;

public class MovePhaseRequestModel {
    private Long toBeInserted;

    public MovePhaseRequestModel() {
    }

    public MovePhaseRequestModel(Long toBeInserted) {
        this.toBeInserted = toBeInserted;
    }

    public Long getToBeInserted() {
        return toBeInserted;
    }

    public void setToBeInserted(Long toBeInserted) {
        this.toBeInserted = toBeInserted;
    }
}
