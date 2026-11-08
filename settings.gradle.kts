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
    repositories { google(); mavenCentral() }
}
rootProject.name = "IU3"
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
include(":app")
include(":core:model")
include(":core:common")
include(":core:data")
include(":core:data-api")
include(":core:notifications")
include(":core:designsystem")
include(":core:navigation")
include(":features:onboarding")
include(":features:home")
include(":features:schedule")
include(":features:department")
include(":features:instructors")
include(":features:courses")
include(":features:honor")
include(":features:admissions")
include(":features:alumni")
include(":features:search")
include(":features:profile")
