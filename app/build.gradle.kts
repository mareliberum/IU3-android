plugins {
    alias(libs.plugins.iu3.android.application)
    alias(libs.plugins.iu3.android.compose)
    alias(libs.plugins.iu3.android.hilt)
}

android {
    namespace = "com.bmstu.iu3"
    defaultConfig {
        applicationId = "com.bmstu.iu3"
        versionCode = 1
        versionName = "1.0"
    }
}

dependencies {
    implementation(projects.core.common)
    implementation(projects.core.dataApi)
    implementation(projects.core.data)
    implementation(projects.core.model)
    implementation(projects.core.notifications)
    implementation(projects.core.designsystem)
    implementation(projects.core.navigation)
    implementation(projects.features.onboarding)
    implementation(projects.features.home)
    implementation(projects.features.schedule)
    implementation(projects.features.department)
    implementation(projects.features.instructors)
    implementation(projects.features.courses)
    implementation(projects.features.honor)
    implementation(projects.features.admissions)
    implementation(projects.features.alumni)
    implementation(projects.features.search)
    implementation(projects.features.profile)
    implementation(libs.activity.compose)
    implementation(libs.compose.navigation.suite)
    implementation(libs.coroutines)
    implementation(libs.navigation.ui)
    implementation(libs.lifecycle.navigation)
    implementation(libs.hilt.work)
    implementation(libs.work)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso)
}
