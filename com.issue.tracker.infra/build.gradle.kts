group = "com.issue.tracker"

plugins {
    alias(libs.plugins.kotlin.jvm)
    id("java")
}

dependencies {
    compileOnly(project(":com.issue.tracker.api"))

    implementation(kotlin("stdlib-jdk8"))
    implementation(libs.slf4j.log4j)
    compileOnly(libs.jakarta.ee)
    implementation(libs.spring.security.crypto)
}

tasks.jar {
    enabled = false
}