plugins {
    alias(libs.plugins.iu3.android.library)
    alias(libs.plugins.iu3.android.compose)
}

android {
    namespace = "com.bmstu.iu3.core.designsystem"
}

dependencies {
    implementation(projects.core.model)
    implementation(libs.coroutines)
    api(libs.coil)
    implementation(libs.coil.network)
}
