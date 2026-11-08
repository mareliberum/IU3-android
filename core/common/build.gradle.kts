plugins {
    alias(libs.plugins.iu3.android.library)
    alias(libs.plugins.iu3.android.hilt)
}

android {
    namespace = "com.bmstu.iu3.core.common"
}

dependencies {
    api(libs.coroutines)
}
