plugins {
    id("net.fabricmc.fabric-loom-remap") version "1.16-SNAPSHOT" apply false
    id("net.fabricmc.fabric-loom") version "1.16-SNAPSHOT" apply false
    id("java")
}

tasks.register("buildAll") {
    dependsOn(subprojects.filter { it.childProjects.isEmpty() }.map { it.tasks.named("build") })
}
