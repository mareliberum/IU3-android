plugins {
    alias(libs.plugins.iu3.android.library)
    alias(libs.plugins.iu3.android.hilt)
    alias(libs.plugins.iu3.android.serialization)
}

android {
    namespace = "com.bmstu.iu3.core.data"
}

dependencies {
    implementation(projects.core.common)
    implementation(projects.core.dataApi)
    implementation(projects.core.model)
    implementation(libs.serialization.json)
    implementation(libs.coroutines)
    implementation(libs.datastore)
}
