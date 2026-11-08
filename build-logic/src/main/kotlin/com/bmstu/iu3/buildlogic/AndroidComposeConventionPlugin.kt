package com.bmstu.iu3.buildlogic

import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

class AndroidComposeConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        pluginManager.apply("org.jetbrains.kotlin.plugin.compose")
        pluginManager.withPlugin("com.android.application") {
            extensions.configure<ApplicationExtension> { buildFeatures.compose = true }
        }
        pluginManager.withPlugin("com.android.library") {
            extensions.configure<LibraryExtension> { buildFeatures.compose = true }
        }
        dependencies.add("implementation", dependencies.platform(libs.library("compose-bom")))
        listOf(
            "compose-ui",
            "compose-foundation",
            "compose-material3",
            "compose-icons",
            "lifecycle-compose",
            "lifecycle-viewmodel",
        ).forEach { alias -> dependencies.add("implementation", libs.library(alias)) }
    }
}
