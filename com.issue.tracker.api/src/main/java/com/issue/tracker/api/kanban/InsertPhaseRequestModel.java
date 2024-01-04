package com.issue.tracker.api.kanban;

import java.io.Serializable;

public class InsertPhaseRequestModel implements Serializable {
    private Long toBeInserted;

    private Long firstPhase;

    private Long secondPhase;

    public InsertPhaseRequestModel() {

    }

    public InsertPhaseRequestModel(Long toBeInserted, Long firstPhase, Long secondPhase) {
        this.toBeInserted = toBeInserted;
        this.firstPhase = firstPhase;
        this.secondPhase = secondPhase;
    }

    public Long getToBeInserted() {
        return toBeInserted;
    }

    public void setToBeInserted(Long toBeInserted) {
        this.toBeInserted = toBeInserted;
    }

    public Long getFirstPhase() {
        return firstPhase;
    }

    public void setFirstPhase(Long firstPhase) {
        this.firstPhase = firstPhase;
    }

    public Long getSecondPhase() {
        return secondPhase;
    }

    public void setSecondPhase(Long secondPhase) {
        this.secondPhase = secondPhase;
    }
}
