import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    `kotlin-dsl`
}

java {
    sourceCompatibility = JavaVersion.toVersion(libs.versions.jvm.get())
    targetCompatibility = JavaVersion.toVersion(libs.versions.jvm.get())
}

kotlin {
    compilerOptions {
        jvmTarget = JvmTarget.fromTarget(libs.versions.jvm.get())
    }
}

dependencies {
    implementation(libs.android.gradle.plugin)
    implementation(libs.kotlin.compose.gradle.plugin)
    implementation(libs.kotlin.serialization.gradle.plugin)
    implementation(libs.hilt.gradle.plugin)
    implementation(libs.ksp.gradle.plugin)
}

gradlePlugin {
    plugins {
        register("androidApplication") {
            id = "iu3.android.application"
            implementationClass = "com.bmstu.iu3.buildlogic.AndroidApplicationConventionPlugin"
        }
        register("androidLibrary") {
            id = "iu3.android.library"
            implementationClass = "com.bmstu.iu3.buildlogic.AndroidLibraryConventionPlugin"
        }
        register("androidApi") {
            id = "iu3.android.api"
            implementationClass = "com.bmstu.iu3.buildlogic.AndroidApiConventionPlugin"
        }
        register("androidFeature") {
            id = "iu3.android.feature"
            implementationClass = "com.bmstu.iu3.buildlogic.AndroidFeatureConventionPlugin"
        }
        register("androidCompose") {
            id = "iu3.android.compose"
            implementationClass = "com.bmstu.iu3.buildlogic.AndroidComposeConventionPlugin"
        }
        register("androidHilt") {
            id = "iu3.android.hilt"
            implementationClass = "com.bmstu.iu3.buildlogic.AndroidHiltConventionPlugin"
        }
        register("androidSerialization") {
            id = "iu3.android.serialization"
            implementationClass = "com.bmstu.iu3.buildlogic.AndroidSerializationConventionPlugin"
        }
    }
}
