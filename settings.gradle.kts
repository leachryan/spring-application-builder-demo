rootProject.name = "spring.application.builder.demo"

pluginManagement {

    repositories {
        gradlePluginPortal()
        mavenLocal()
        mavenCentral()
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.5.0"
}

dependencyResolutionManagement {

    repositories {
        gradlePluginPortal()
        mavenCentral()
    }

    versionCatalogs {
        create("testLibs") {
            from(files("./gradle/testLibs.versions.toml"))
        }
    }
}

include(":application")
include(":api:echo")
include(":api:actuator")
