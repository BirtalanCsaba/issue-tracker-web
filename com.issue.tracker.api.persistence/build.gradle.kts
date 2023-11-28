group = "com.issue.tracker"

plugins {
    id("java")
}

dependencies {
    implementation(project(":com.issue.tracker.domain"))

    compileOnly(libs.jakarta.ee)
}