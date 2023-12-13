package com.issue.tracker.infra.persistence.user;

import com.issue.tracker.infra.persistence.common.BaseEntity;
import com.issue.tracker.infra.persistence.kanban.IssueEntity;
import com.issue.tracker.infra.persistence.kanban.KanbanEntity;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
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

    @Column(length = 32)
    private String emailConfirmationToken;

    @Column
    private Boolean activated = false;

    @ManyToMany
    @JoinTable(
            name = "users_issues",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "issue_id"))
    private List<IssueEntity> issues;

    @OneToMany(mappedBy = "kanban")
    private Set<KanbanEntity> kanbans = new HashSet<>();

    public UserEntity() {

    }

    public UserEntity(String username, String password, String firstName, String lastName, String email, String emailConfirmationToken) {
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.emailConfirmationToken = emailConfirmationToken;
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

    public Boolean getActivated() {
        return activated;
    }

    public void setActivated(Boolean activated) {
        this.activated = activated;
    }

    public String getEmailConfirmationToken() {
        return emailConfirmationToken;
    }

    public void setEmailConfirmationToken(String emailConfirmationToken) {
        this.emailConfirmationToken = emailConfirmationToken;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<IssueEntity> getIssues() {
        return issues;
    }

    public void setIssues(List<IssueEntity> issues) {
        this.issues = issues;
    }

    public Set<KanbanEntity> getKanbans() {
        return kanbans;
    }

    public void setKanbans(Set<KanbanEntity> kanbans) {
        this.kanbans = kanbans;
    }
}
