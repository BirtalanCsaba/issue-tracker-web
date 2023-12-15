package com.issue.tracker.infra.web.config;

import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;

@ApplicationPath("/api/v1")
public class IssueTrackerApplication extends Application {
}
