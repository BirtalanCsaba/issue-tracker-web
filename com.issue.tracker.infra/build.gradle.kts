group = "com.issue.tracker"

plugins {
    id("java")
}

dependencies {
    implementation(project(":com.issue.tracker.api"))

    implementation(libs.slf4j.log4j)
    compileOnly(libs.jakarta.ee)
    implementation(libs.spring.security.crypto)
    implementation(libs.auth0.jwt)
}