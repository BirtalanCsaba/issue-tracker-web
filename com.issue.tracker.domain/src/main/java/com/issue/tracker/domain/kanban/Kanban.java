package com.issue.tracker.domain.kanban;

import java.util.List;

public class Kanban {
    private String title;

    private String description;

    private Long owner;

    private List<Long> admins;

    private List<Long> participants;

    public Kanban(String title, String description, Long owner, List<Long> admins, List<Long> participants) {
        this.title = title;
        this.description = description;
        this.owner = owner;
        this.admins = admins;
        this.participants = participants;
    }

    public Kanban(String title, String description) {
        this.title = title;
        this.description = description;
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

    public Long getOwner() {
        return owner;
    }

    public void setOwner(Long owner) {
        this.owner = owner;
    }

    public List<Long> getAdmins() {
        return admins;
    }

    public void setAdmins(List<Long> admins) {
        this.admins = admins;
    }

    public List<Long> getParticipants() {
        return participants;
    }

    public void setParticipants(List<Long> participants) {
        this.participants = participants;
    }
}
