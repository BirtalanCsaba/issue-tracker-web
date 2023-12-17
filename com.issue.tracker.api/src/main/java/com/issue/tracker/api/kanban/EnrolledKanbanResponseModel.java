package com.issue.tracker.api.kanban;

import java.util.List;

public class EnrolledKanbanResponseModel extends KanbanResponseModel {
    private String role;

    public EnrolledKanbanResponseModel() {

    }

    public EnrolledKanbanResponseModel(Long id, String title, String description, List<Long> admins, List<Long> participants, String role) {
        super(id, title, description, admins, participants);
        this.role = role;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
