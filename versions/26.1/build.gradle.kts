plugins {
    id("net.fabricmc.fabric-loom")
}

val minecraft_version: String by project
val fabric_api_version: String by project
val fabric_loader_version: String by project
val java_version: String by project
val minecraft_range: String by project
val carpet_version: String by project
val permissions_api_version: String by project

version = rootProject.property("mod_version") as String
group = rootProject.property("mod_group") as String
base.archivesName.set(rootProject.property("mod_id") as String)

repositories {
    mavenCentral()
    maven("https://maven.fabricmc.net/")
    maven("https://api.modrinth.com/maven")
    maven("https://cursemaven.com") {
        isAllowInsecureProtocol = true
    }
}

dependencies {
    implementation(project(":common"))
    "minecraft"("com.mojang:minecraft:$minecraft_version")
    implementation("net.fabricmc:fabric-loader:$fabric_loader_version")
    implementation("net.fabricmc.fabric-api:fabric-api:$fabric_api_version")
    implementation("maven.modrinth:fabric-permissions-api:$permissions_api_version")
    implementation("maven.modrinth:TQTTVgYE:$carpet_version")
}

tasks.processResources {
    inputs.property("version", project.version)
    inputs.property("minecraft_version", minecraft_version)
    inputs.property("loader_version", fabric_loader_version)
    inputs.property("java_version", java_version)
    inputs.property("minecraft_range", minecraft_range)

    filesMatching("fabric.mod.json") {
        expand(
            "version" to project.version,
            "minecraft_version" to minecraft_version,
            "loader_version" to fabric_loader_version,
            "java_version" to java_version,
            "minecraft_range" to minecraft_range
        )
    }
}

tasks.withType<JavaCompile> {
    options.release.set(java_version.toInt())
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(java_version.toInt()))
    }
}

tasks.jar {
    archiveVersion.set("${project.version}-mc$minecraft_version")
}
