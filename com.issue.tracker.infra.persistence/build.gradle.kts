group = "com.issue.tracker"

plugins {
    id("java")
}

dependencies {
    implementation(project(":com.issue.tracker.api.persistence"))
    implementation(project(":com.issue.tracker.api"))
    implementation(project(":com.issue.tracker.infra"))

    compileOnly(libs.jakarta.ee)
    annotationProcessor(libs.hibernate.jpamodelgen)
    implementation(libs.jetbrains.kotlinx.datetime)
}