group = "com.issue.tracker"

plugins {
    alias(libs.plugins.kotlin.jvm)
    id("java")
}

dependencies {
    compileOnly(project(":com.issue.tracker.api"))

    implementation(kotlin("stdlib-jdk8"))
    compileOnly(libs.jakarta.ee)
    implementation(libs.fasterxml.jackson)
    implementation(libs.fasterxml.jackson.databind)
}

tasks.jar {
    enabled = false
}