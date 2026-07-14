pluginManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
        maven("https://maven.fabricmc.net/") {
            name = "Fabric"
        }
    }
}

rootProject.name = "carpet-permissions"

include("common")
include("versions:1.21.11")
include("versions:26.1")
