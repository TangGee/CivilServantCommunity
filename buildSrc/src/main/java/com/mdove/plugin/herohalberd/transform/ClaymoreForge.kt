package com.mdove.plugin.herohalberd.transform

import com.mdove.plugin.herohalberd.readBytesCompat
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassWriter
import org.objectweb.asm.util.CheckClassAdapter
import java.io.File
import java.io.FileOutputStream
import java.util.jar.JarFile
import java.util.jar.JarOutputStream
import java.util.zip.ZipEntry

object ClaymoreForge {
    private const val CLAYMORE_SERVICE_LOADER_NAME = "com/mdove/herohalberd/ClaymoreServiceLoader.class"

    fun clear() {
        ClaymoreElementCollector.clear()
    }

    fun collectClaymoreElement(source: File) {
        when {
            source.isDirectory -> {
                source.walkTopDown()
                    .asSequence()
                    .filter { ClaymoreElementCollector.filterPackage(it.name) && ClaymoreElementCollector.filterClass(it.name) }
                    .forEach {
                        ClassReader(it.readBytes()).accept(ClaymoreElementCollector, 0)
                    }
            }
            else -> {
                JarFile(source).run {
                    this.entries()
                        .iterator()
                        .asSequence()
                        .filter { ClaymoreElementCollector.filterPackage(it.name) && ClaymoreElementCollector.filterClass(it.name) }
                        .forEach { entry ->
                            this.getInputStream(entry).use { inputStream ->
                                if (inputStream != null) {
                                    ClassReader(inputStream.readBytesCompat()).accept(
                                        ClaymoreElementCollector, 0)
                                }
                            }
                        }
                }
            }
        }
    }

    fun injectClaymoreMapping(source: File): File {
        if (!source.isDirectory) {
            JarFile(source).let { jarFile ->
                jarFile.entries()
                    .iterator()
                    .asSequence()
                    .find {
                        it.name == CLAYMORE_SERVICE_LOADER_NAME
                    }
                    ?.let { entry ->
                        ClassWriter(0).also { writer ->
                            jarFile.getInputStream(entry).use { inputStream ->
                                ClassReader(inputStream.readBytesCompat())
                                    .accept(
                                        ClaymoreServiceLoaderWriter(
                                            ClaymoreElementCollector.claymoreElements,
                                            writer
                                        ).let { CheckClassAdapter(it) }, 0
                                    )
                            }
                        }
                    }?.let { classWriter ->
                        val tempJar = File(source.absolutePath.replace(".jar", ".temp.jar"))
                        if (tempJar.exists()) {
                            tempJar.delete()
                        }

                        JarOutputStream(FileOutputStream(tempJar)).use { jarOutputStream ->
                            jarFile.entries().iterator().forEach { jarEntry ->
                                jarOutputStream.putNextEntry(ZipEntry(jarEntry.name))
                                if (jarEntry.name == CLAYMORE_SERVICE_LOADER_NAME) {
                                    jarOutputStream.write(classWriter.toByteArray())
                                } else {
                                    jarFile.getInputStream(jarEntry).use {
                                        jarOutputStream.write(it.readBytesCompat())
                                    }
                                }
                                jarOutputStream.closeEntry()
                            }
                        }
                        return tempJar
                    }

            }
        }
        return source
    }
}