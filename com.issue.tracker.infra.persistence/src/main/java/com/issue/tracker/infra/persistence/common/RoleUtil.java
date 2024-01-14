package com.issue.tracker.infra.persistence.common;

import com.issue.tracker.infra.persistence.kanban.KanbanUserRole;

import java.util.Objects;

public class RoleUtil {
    public static KanbanUserRole convertStringToRole(String role) {
        if (Objects.equals(role, "ADMIN")) {
            return KanbanUserRole.ADMIN;
        }
        if (Objects.equals(role, "PARTICIPANT")) {
            return KanbanUserRole.PARTICIPANT;
        }
        throw new RuntimeException("invalid role");
    }

    public static String convertRoleToString(KanbanUserRole role) {
        if (role == KanbanUserRole.ADMIN) {
            return "ADMIN";
        }
        return "PARTICIPANT";
    }
}
