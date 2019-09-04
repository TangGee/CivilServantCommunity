package com.mdove.plugin.herohalberd.transform

import com.android.build.api.transform.Format
import com.android.build.api.transform.QualifiedContent
import com.android.build.api.transform.Transform
import com.android.build.api.transform.TransformInvocation
import com.android.build.gradle.internal.pipeline.TransformManager
import com.android.utils.FileUtils
import java.io.File

class ClaymoreTransform : Transform() {
    override fun getName(): String {
        return "claymore"
    }

    override fun getInputTypes(): MutableSet<QualifiedContent.ContentType> {
        return TransformManager.CONTENT_CLASS
    }

    override fun isIncremental(): Boolean {
        return false
    }

    override fun getScopes(): MutableSet<in QualifiedContent.Scope> {
        return TransformManager.SCOPE_FULL_PROJECT
    }

    override fun transform(transformInvocation: TransformInvocation?) {
        ClaymoreForge.clear()

        transformInvocation?.inputs?.forEach { input ->
            input.directoryInputs.forEach {
                ClaymoreForge.collectClaymoreElement(it.file)
            }
            input.jarInputs.forEach {
                ClaymoreForge.collectClaymoreElement(it.file)
            }
        }

        fun QualifiedContent.getContentLocation(transformInvocation: TransformInvocation, format: Format): File {
            return transformInvocation.outputProvider.getContentLocation(
                this.name,
                this.contentTypes,
                this.scopes,
                format)
        }

        transformInvocation?.inputs?.forEach { input ->

            input.directoryInputs.forEach {
                FileUtils.copyDirectory(
                    it.file,
                    it.getContentLocation(transformInvocation, Format.DIRECTORY)
                )
            }

            input.jarInputs.forEach {
                FileUtils.copyFile(
                    ClaymoreForge.injectClaymoreMapping(it.file),
                    it.getContentLocation(transformInvocation, Format.JAR)
                )
            }
        }
    }
}
