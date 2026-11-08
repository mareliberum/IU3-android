package com.bmstu.iu3.buildlogic

import org.gradle.api.Plugin
import org.gradle.api.Project

class AndroidHiltConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        pluginManager.apply("com.google.devtools.ksp")
        pluginManager.apply("com.google.dagger.hilt.android")
        dependencies.add("implementation", libs.library("hilt-android"))
        dependencies.add("ksp", libs.library("hilt-compiler"))
        pluginManager.withPlugin("iu3.android.feature") {
            dependencies.add("implementation", libs.library("hilt-compose"))
        }
    }
}
