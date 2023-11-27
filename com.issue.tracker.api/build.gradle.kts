group = "com.issue.tracker"

plugins {
    id("java")
}

dependencies {
    implementation(project(":com.issue.tracker.domain"))
    implementation(project(":com.issue.tracker.api.persistence"))

    compileOnly(libs.jakarta.ee)
}

