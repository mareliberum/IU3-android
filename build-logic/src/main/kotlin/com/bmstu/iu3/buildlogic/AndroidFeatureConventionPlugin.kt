package com.bmstu.iu3.buildlogic

import org.gradle.api.Plugin
import org.gradle.api.Project

class AndroidFeatureConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        pluginManager.apply(AndroidLibraryConventionPlugin::class.java)
        pluginManager.apply(AndroidComposeConventionPlugin::class.java)
    }
}
