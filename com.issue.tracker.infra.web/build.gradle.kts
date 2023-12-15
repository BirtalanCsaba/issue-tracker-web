group = "com.issue.tracker"

plugins {
    id("java")
    war
}

dependencies {
    implementation(project(":com.issue.tracker.infra"))
    implementation(project(":com.issue.tracker.api"))

    compileOnly(libs.jakarta.ee)
    implementation(libs.fasterxml.jackson)
    implementation(libs.fasterxml.jackson.databind)
}