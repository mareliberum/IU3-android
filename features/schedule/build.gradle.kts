plugins {
    alias(libs.plugins.iu3.android.feature)
    alias(libs.plugins.iu3.android.hilt)
}

android {
    namespace = "com.bmstu.iu3.features.schedule"
}

dependencies {
    implementation(projects.core.dataApi)
    implementation(projects.core.model)
    implementation(projects.core.designsystem)
    implementation(projects.core.navigation)
}
