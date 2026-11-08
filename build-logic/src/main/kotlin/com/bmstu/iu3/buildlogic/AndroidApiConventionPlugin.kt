package com.bmstu.iu3.buildlogic

import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

class AndroidApiConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        pluginManager.apply(AndroidLibraryConventionPlugin::class.java)
        extensions.configure<LibraryExtension> {
            androidResources.enable = false
        }
    }
}
