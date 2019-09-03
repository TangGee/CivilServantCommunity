package com.mdove.plugin.herohalberd

import com.android.build.gradle.AppExtension
import com.android.build.gradle.AppPlugin
import com.mdove.plugin.herohalberd.transform.ClaymoreTransform
import org.gradle.api.Plugin
import org.gradle.api.Project

open class ClaymorePlugin : Plugin<Project> {
    override fun apply(project: Project) {
        if (project.plugins.find { it is AppPlugin } == null) {
            return
        }
        project.extensions.getByType(AppExtension::class.java)?.run {
            registerTransform(ClaymoreTransform())
        }
    }
}