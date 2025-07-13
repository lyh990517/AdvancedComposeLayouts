pluginManagement {
    includeBuild("build-logic")
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
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "AdvancedComposeLayouts"
include(":app")
include(":ui")
include(":ui:3d-pages")
include(":ui:metallic-shader-card")
include(":ui:animated-gradient")
include(":ui:pull-to-refresh-with-nested-scroll")
