group = "com.issue.tracker"

plugins {
    alias(libs.plugins.kotlin.jvm)
    id("java")
}

dependencies {
    compileOnly(project(":com.issue.tracker.api.persistence"))
    compileOnly(project(":com.issue.tracker.infra"))

    implementation(kotlin("stdlib-jdk8"))
    compileOnly(libs.jakarta.ee)
    annotationProcessor(libs.hibernate.jpamodelgen)
    implementation(libs.jetbrains.kotlinx.datetime)
}

tasks.jar {
    enabled = false
}