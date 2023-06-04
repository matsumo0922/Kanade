pluginManagement {
    includeBuild("build-logic")
    repositories {
        google()
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

rootProject.name = "KanadeMark3"

include(":app")
include(":core")
include(":core:common")
include(":core:design")
include(":core:model")
include(":core:datastore")
include(":core:repository")
include(":feature:home")
include(":feature:playlist")
include(":feature:song")
include(":feature:artist")
include(":feature:album")
include(":core:ui")
include(":core:music")
include(":core:database")
