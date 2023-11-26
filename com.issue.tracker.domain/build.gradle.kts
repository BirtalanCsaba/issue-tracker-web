group = "com.issue.tracker"

plugins {
    alias(libs.plugins.kotlin.jvm)
    id("java")
}

dependencies {
    compileOnly(kotlin("stdlib-jdk8"))
}

tasks.jar {
    enabled = false
}