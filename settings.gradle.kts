pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "MoisesChallengeApp"

// Main app modules
include(":app")
include(":automotive")

// Core modules
include(":core:domain")
include(":core:data")
include(":core:network")
include(":core:player")
include(":core:ui")
include(":core:resources")

// Shared feature modules
include(":shared:player")
include(":shared:album")
