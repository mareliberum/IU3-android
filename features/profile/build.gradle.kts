plugins {
    alias(libs.plugins.iu3.android.feature)
    alias(libs.plugins.iu3.android.hilt)
}

android {
    namespace = "com.bmstu.iu3.features.profile"
}

dependencies {
    implementation(projects.core.common)
    implementation(projects.core.dataApi)
    implementation(projects.core.model)
    implementation(projects.core.notifications)
    implementation(projects.core.designsystem)
    implementation(projects.core.navigation)
    implementation(libs.activity.compose)
}
