plugins {
    alias(libs.plugins.iu3.android.api)
    alias(libs.plugins.iu3.android.serialization)
}

android {
    namespace = "com.bmstu.iu3.core.navigation"
}

dependencies {
    implementation(projects.core.model)
    implementation(libs.serialization.json)
    api(libs.navigation.runtime)
}
