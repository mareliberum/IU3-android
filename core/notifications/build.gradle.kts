plugins {
    alias(libs.plugins.iu3.android.library)
    alias(libs.plugins.iu3.android.hilt)
}

android {
    namespace = "com.bmstu.iu3.core.notifications"
}

dependencies {
    implementation(projects.core.common)
    implementation(projects.core.dataApi)
    implementation(libs.coroutines)
    implementation(libs.work)
    implementation(libs.hilt.work)
    ksp(libs.hilt.work.compiler)
}
