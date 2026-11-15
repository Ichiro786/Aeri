pluginManagement {
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

rootProject.name = "AeriAndroid"
include(":shared", ":sync", ":browser", ":store", ":blocking", ":design", ":appmodel", ":browsing", ":search", ":tabs", ":settings", ":app")
