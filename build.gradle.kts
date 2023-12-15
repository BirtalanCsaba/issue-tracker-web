import java.util.*

plugins {
    id("java")
    ear
}

dependencies {
    implementation(project(":com.issue.tracker.domain"))
    implementation(project(":com.issue.tracker.api"))
    implementation(project(":com.issue.tracker.api.persistence"))
    implementation(project(":com.issue.tracker.infra"))
    implementation(project(":com.issue.tracker.infra.web"))
    implementation(project(":com.issue.tracker.infra.persistence"))

    deploy(project(":com.issue.tracker.domain"))
    deploy(project(":com.issue.tracker.api"))
    deploy(project(":com.issue.tracker.api.persistence"))
    deploy(project(":com.issue.tracker.infra"))
    deploy(project(":com.issue.tracker.infra.web")) {
        targetConfiguration = "archives"
    }
    deploy(project(":com.issue.tracker.infra.persistence"))

    earlib(group = libs.jakarta.ee.get().group, name = libs.jakarta.ee.get().name, version = libs.jakarta.ee.get().version, ext = "jar")
    earlib(group = libs.slf4j.log4j.get().group, name = libs.slf4j.log4j.get().name, version = libs.slf4j.log4j.get().version, ext = "jar")
    earlib(group = libs.hibernate.jpamodelgen.get().group, name = libs.hibernate.jpamodelgen.get().name, version = libs.hibernate.jpamodelgen.get().version, ext = "jar")
    earlib(group = libs.jetbrains.kotlinx.datetime.get().group, name = libs.jetbrains.kotlinx.datetime.get().name, version = libs.jetbrains.kotlinx.datetime.get().version, ext = "jar")
//    earlib(group = libs.fasterxml.jackson.databind.get().group, name = libs.fasterxml.jackson.databind.get().name, version = libs.fasterxml.jackson.databind.get().version, ext = "jar")
    earlib(group = libs.spring.security.crypto.get().group, name = libs.spring.security.crypto.get().name, version = libs.spring.security.crypto.get().version, ext = "jar")
    earlib(group = libs.auth0.jwt.get().group, name = libs.auth0.jwt.get().name, version = libs.auth0.jwt.get().version, ext = "jar")
}

val buildTask by tasks.named("build")

open class DeployProperties {
    var scriptExtension: String? = null
    var wildflyInstallDirectory: String? = null
}

val deployPropertiesExtension = project.extensions.create("deployPropertiesExtension", DeployProperties::class)

val loadPropertiesTask by tasks.register("loadProperties") {
    val customBuildProperties = Properties()
    customBuildProperties.load(file("build.properties").reader())

    val wildflyInstallDirectory: String? = customBuildProperties.getProperty("wildflyInstallDirectory")
        ?: null
    if (wildflyInstallDirectory != null) {
        deployPropertiesExtension.wildflyInstallDirectory = wildflyInstallDirectory
        project.logger.lifecycle("Loaded (wildflyInstallDirectory): $wildflyInstallDirectory")
    }

    val scriptExtension: String? = customBuildProperties.getProperty("scriptExtension")
        ?: null
    if (scriptExtension != null) {
        deployPropertiesExtension.scriptExtension = scriptExtension
        project.logger.lifecycle("Loaded (scriptExtension): $scriptExtension")
    }
}

tasks.register("startWildfly") {
    group = "server"
    dependsOn(loadPropertiesTask)
    doLast {
        val wildflyInstallDirectory = deployPropertiesExtension.wildflyInstallDirectory
        if (wildflyInstallDirectory == null) {
            project.logger.error("Operation failed. Undefined (wildflyInstallDirectory) property!")
            return@doLast
        }
        val scriptExtension = deployPropertiesExtension.scriptExtension
        if (scriptExtension == null) {
            project.logger.error("Deploy failed. Undefined (scriptExtension) property!")
            return@doLast
        }

        val execResult = exec {
            commandLine(
                "$wildflyInstallDirectory/bin/standalone.$scriptExtension"
            )
        }
        if (execResult.exitValue == 1) {
            project.logger.error("Operation failed. Could not start the server!")
            return@doLast
        }
        project.logger.lifecycle("Wildfly server started successfully!")
    }
}

tasks.register("stopWildfly") {
    group = "server"
    dependsOn(loadPropertiesTask)
    doLast {
        val wildflyInstallDirectory = deployPropertiesExtension.wildflyInstallDirectory
        if (wildflyInstallDirectory == null) {
            project.logger.error("Operation failed. Undefined (wildflyInstallDirectory) property!")
            return@doLast
        }
        val scriptExtension = deployPropertiesExtension.scriptExtension
        if (scriptExtension == null) {
            project.logger.error("Deploy failed. Undefined (scriptExtension) property!")
            return@doLast
        }
        val execResult = exec {
            commandLine(
                "$wildflyInstallDirectory/bin/jboss-cli.$scriptExtension",
                "-c",
                "--commands=:shutdown"
            )
        }
        if (execResult.exitValue == 1) {
            project.logger.error("Operation failed. Could not stop the server!")
            return@doLast
        }
        project.logger.lifecycle("Wildfly server stopped successfully!")
    }
}

tasks.register("deployWildfly") {
    group = "deploy"
    dependsOn(buildTask, loadPropertiesTask)
    doLast {
        val wildflyInstallDirectory = deployPropertiesExtension.wildflyInstallDirectory
        if (wildflyInstallDirectory == null) {
            project.logger.error("Deploy failed. Undefined (wildflyInstallDirectory) property!")
            return@doLast
        }
        val copyResult = copy {
            from("build/libs")
            into("$wildflyInstallDirectory/standalone/deployments")
            include("*.ear")
        }
        if (!copyResult.didWork) {
            project.logger.error("Deploy failed. Cannot copy the '.*ear' file to the application server's deploy directory!")
        }
        project.logger.lifecycle("Deploy finished!")
    }
}