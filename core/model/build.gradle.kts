plugins {
    alias(libs.plugins.iu3.android.api)
    alias(libs.plugins.iu3.android.serialization)
}

android {
    namespace = "com.bmstu.iu3.core.model"
}

dependencies {
    api(libs.serialization.json)
}
