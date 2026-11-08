package com.bmstu.iu3.buildlogic

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Project

internal fun Project.configureAndroid(extension: CommonExtension) {
    extension.compileSdk = libs.version("android-compileSdk").toInt()
    extension.defaultConfig.minSdk = libs.version("android-minSdk").toInt()
    extension.compileOptions.apply {
        sourceCompatibility = JavaVersion.toVersion(libs.version("jvm"))
        targetCompatibility = JavaVersion.toVersion(libs.version("jvm"))
    }
}
