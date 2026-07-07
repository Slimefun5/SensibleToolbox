plugins {
    java
    id("com.gradleup.shadow") version "9.3.2"
    id("io.github.intisy.github-gradle") version "1.8.2.1"
}

group = "com.github.slimefun"
description = "SensibleToolbox is a Spigot/Paper plugin which adds tons of items and machines inspired by popular mods."

// Shared Slimefun-addon build conventions (Java 8, spigot-api baseline, core dep, publish, shadow, version).
apply(from = "https://raw.githubusercontent.com/Slimefun5/workflows/stable/slimefun-addon.gradle")

repositories {
    maven("https://jitpack.io")
    maven("https://repo.dmulloy2.net/repository/public/")
    maven("https://maven.enginehub.org/repo/")
}

dependencies {
    githubImplementation("Slimefun5:SlimefunMetrics:v1.0.0")
    // Shaded so the JDBC driver is present on 1.8 servers. 3.42.0.0 is the newest Java-8-compatible release.
    implementation("org.xerial:sqlite-jdbc:3.42.0.0")
    compileOnly("commons-lang:commons-lang:2.6")
    compileOnly("com.comphenix.protocol:ProtocolLib:5.3.0")
    compileOnly("com.gmail.filoghost.holographicdisplays:holographicdisplays-api:2.4.9")
    compileOnly("com.sk89q.worldedit:worldedit-bukkit:7.2.8") {
        exclude(group = "de.schlichtherle", module = "truezip")
    }

    testImplementation(platform("org.junit:junit-bom:5.11.4"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    testImplementation("org.mockito:mockito-core:5.15.2")
    testImplementation("org.slf4j:slf4j-simple:2.0.16")
    testImplementation("org.mockbukkit.mockbukkit:mockbukkit-v1.21:4.107.0") {
        exclude(group = "org.jetbrains", module = "annotations")
    }
}

configurations.testImplementation {
    extendsFrom(configurations.compileOnly.get())
}

tasks {
    shadowJar {
        relocate("org.bstats", "sensibletoolbox.libs.bstats")
    }
    compileTestJava { enabled = false }
    test { enabled = false }
}
