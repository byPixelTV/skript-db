plugins {
    java
    id("net.minecrell.plugin-yml.bukkit") version "0.6.0"
    id("com.gradleup.shadow") version "8.3.5"
    id("io.papermc.paperweight.userdev") version "2.0.0-beta.14"
}

val versionString = "1.6.0"

group = "com.btk5h"
version = versionString

repositories {
    mavenCentral()
    maven("https://jitpack.io")
    maven(url = "https://s01.oss.sonatype.org/content/repositories/snapshots/")
    maven {
        name = "papermc"
        url = uri("https://repo.papermc.io/repository/maven-public/")
    }
    maven {
        url = uri("https://repo.skriptlang.org/releases")
    }
}

dependencies {
    paperweight.paperDevBundle("1.21.4-R0.1-SNAPSHOT")

    compileOnly("com.github.SkriptLang:Skript:2.10.0")
    bukkitLibrary("org.mariadb.jdbc:mariadb-java-client:3.5.1")
    bukkitLibrary("org.postgresql:postgresql:42.7.5")
    bukkitLibrary("com.zaxxer:HikariCP:6.2.1")
    compileOnly("org.jetbrains:annotations:26.0.2")
    implementation("com.github.technicallycoded:FoliaLib:main-SNAPSHOT")
}

sourceSets {
    getByName("main") {
        java {
            srcDir("src/main/java")
        }
    }
}

tasks {
    compileJava {
        options.encoding = "UTF-8"
        options.release.set(21)
    }

    shadowJar {
        archiveBaseName.set("skript-db")
        archiveVersion.set(version.toString())
        archiveClassifier.set("")

        relocate("com.tcoded.folialib", "com.btk5h.skriptdb.lib.folialib")
    }
}

paperweight.reobfArtifactConfiguration = io.papermc.paperweight.userdev.ReobfArtifactConfiguration.MOJANG_PRODUCTION

bukkit {
    main = "com.btk5h.skriptdb.SkriptDB"
    version = versionString
    foliaSupported = true
    apiVersion = "1.13"
    authors = listOf("btk5h", "FranKusmiruk", "Govindas", "TPGamesNL", "byPixelTV")
    description = "A Skript addon that allows you to interact with SQL databases."
    depend = listOf("Skript")
    prefix = "SkriptDB"
    foliaSupported = true
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}