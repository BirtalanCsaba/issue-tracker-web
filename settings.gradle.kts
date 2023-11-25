rootProject.name = "issue-tracker"

pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.PREFER_SETTINGS)
    repositories {
        mavenCentral()
    }
}

include(
    "com.issue.tracker.domain",
    "com.issue.tracker.api",
    "com.issue.tracker.web",
    "com.issue.tracker.api.persistence",
    "com.issue.tracker.domain",
    "com.issue.tracker.app",
    "com.issue.tracker.infra",
    "com.issue.tracker.infra.web",
    "com.issue.tracker.infra.persistence",
)
