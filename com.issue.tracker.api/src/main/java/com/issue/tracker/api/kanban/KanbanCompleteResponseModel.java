package com.issue.tracker.api.kanban;

import com.issue.tracker.api.persistence.auth.UserDsResponseModel;

import java.util.List;

public class KanbanCompleteResponseModel {
    private Long id;

    private String title;

    private String description;

    private List<UserDsResponseModel> admins;

    private List<UserDsResponseModel> participants;

    private UserDsResponseModel owner;

    private List<CompletePhaseResponseModel> phase;

    public KanbanCompleteResponseModel() {
    }

    public KanbanCompleteResponseModel(
            Long id,
            String title,
            String description,
            List<UserDsResponseModel> admins,
            List<UserDsResponseModel> participants,
            UserDsResponseModel owner,
            List<CompletePhaseResponseModel> phase
    ) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.admins = admins;
        this.participants = participants;
        this.owner = owner;
        this.phase = phase;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<UserDsResponseModel> getAdmins() {
        return admins;
    }

    public void setAdmins(List<UserDsResponseModel> admins) {
        this.admins = admins;
    }

    public List<UserDsResponseModel> getParticipants() {
        return participants;
    }

    public void setParticipants(List<UserDsResponseModel> participants) {
        this.participants = participants;
    }

    public UserDsResponseModel getOwner() {
        return owner;
    }

    public void setOwner(UserDsResponseModel owner) {
        this.owner = owner;
    }

    public List<CompletePhaseResponseModel> getPhase() {
        return phase;
    }

    public void setPhase(List<CompletePhaseResponseModel> phase) {
        this.phase = phase;
    }
}
