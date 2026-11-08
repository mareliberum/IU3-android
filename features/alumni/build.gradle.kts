plugins {
    alias(libs.plugins.iu3.android.feature)
}

android {
    namespace = "com.bmstu.iu3.features.alumni"
}

dependencies {
    implementation(projects.core.model)
    implementation(projects.core.designsystem)
    implementation(projects.core.navigation)
}
