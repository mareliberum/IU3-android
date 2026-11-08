plugins {
    alias(libs.plugins.iu3.android.api)
}

android {
    namespace = "com.bmstu.iu3.core.data.api"
}

dependencies {
    api(projects.core.model)
    api(libs.coroutines)
}
