package com.issue.tracker.infra.persistence.user;

import com.issue.tracker.infra.persistence.common.BaseEntity;
import com.issue.tracker.infra.persistence.kanban.IssueEntity;
import com.issue.tracker.infra.persistence.kanban.KanbanEntity;
import com.issue.tracker.infra.persistence.kanban.KanbanUserEntity;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "application_user")
public class UserEntity extends BaseEntity<Long> {
    @Column(unique = true, length = 60)
    private String username;

    @Column(length = 72)
    private String password;

    @Column(length = 80)
    private String firstName;

    @Column(length = 80)
    private String lastName;

    @Column(length = 255, unique = true)
    private String email;

    @OneToMany(
            mappedBy = "user",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<KanbanUserEntity> kanbans;

    @OneToMany(
            mappedBy = "owner",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<KanbanEntity> ownerKanbans;

    public UserEntity() {

    }

    public UserEntity(String username, String password, String firstName, String lastName, String email) {
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<KanbanUserEntity> getKanbans() {
        return kanbans;
    }

    public void setKanbans(List<KanbanUserEntity> kanbans) {
        this.kanbans = kanbans;
    }
}
