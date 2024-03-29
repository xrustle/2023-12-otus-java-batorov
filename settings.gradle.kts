rootProject.name = "otus"
include("hw01-gradle")
include("hw04-generics")
include("hw06-reflection")
include("hw08-garbage-collector")
include("hw10-aspect-oriented-programming")
include("hw12-solid-principles")
include("hw15-patterns")
include("hw16-serialization")
include("hw18-jdbc")
include("hw21-hibernate")

pluginManagement {
    val dependencyManagement: String by settings
    val lombok: String by settings
    val johnrengelmanShadow: String by settings
    val springframeworkBoot: String by settings
    val sonarlint: String by settings
    val spotless: String by settings

    plugins {
        id("io.spring.dependency-management") version dependencyManagement
        id("io.freefair.lombok") version lombok
        id("com.github.johnrengelman.shadow") version johnrengelmanShadow
        id("org.springframework.boot") version springframeworkBoot
        id("name.remal.sonarlint") version sonarlint
        id("com.diffplug.spotless") version spotless
    }
}