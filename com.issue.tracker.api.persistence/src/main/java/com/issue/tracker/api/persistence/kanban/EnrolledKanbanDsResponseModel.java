package com.issue.tracker.api.persistence.kanban;

import java.util.List;

public class EnrolledKanbanDsResponseModel extends KanbanDsResponseModel {
    private String role;

    public EnrolledKanbanDsResponseModel() {

    }

    public EnrolledKanbanDsResponseModel(Long id, String title, String description, Long ownerId, List<Long> admins, List<Long> participants, String role) {
        super(id, title, description, ownerId, admins, participants);
        this.role = role;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
