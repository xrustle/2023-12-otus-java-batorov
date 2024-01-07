rootProject.name = "otus"
include("hw01-gradle")

pluginManagement {
    val dependencyManagement: String by settings
    val johnrengelmanShadow: String by settings

    plugins {
        id("io.spring.dependency-management") version dependencyManagement
        id("com.github.johnrengelman.shadow") version johnrengelmanShadow
    }
}