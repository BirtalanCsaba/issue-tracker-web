group = "com.issue.tracker"

plugins {
    alias(libs.plugins.kotlin.jvm)
    id("java")
}

dependencies {
    compileOnly(project(":com.issue.tracker.domain"))
    compileOnly(project(":com.issue.tracker.api.persistence"))

    implementation(kotlin("stdlib-jdk8"))
    implementation(libs.jetbrains.kotlinx.datetime)
}

tasks.jar {
    enabled = false
}
