group = "com.issue.tracker"

plugins {
    alias(libs.plugins.kotlin.jvm)
    id("java")
}

dependencies {
    compileOnly(project(":com.issue.tracker.domain"))

    implementation(kotlin("stdlib-jdk8"))
}

tasks.jar {
    enabled = false
}