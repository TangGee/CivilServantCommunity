package com.mdove.plugin.herohalberd.transform

import org.objectweb.asm.AnnotationVisitor
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.Opcodes
import org.objectweb.asm.Type

object ClaymoreElementCollector : ClassVisitor(Opcodes.ASM5) {

    const val CLAYMORE_IMPLEMENTATION_ANNOTATION_DESC = "Lcom/mdove/herohalberd/ClaymoreImpl;"
    const val CLAYMORE_NOOP_ANNOTATION_DESC = "Lcom/mdove/herohalberd/ClaymoreNoop;"

    var claymoreElements = mutableListOf<ClaymoreElement>()
        private set

    var clazzName: String? = null

    override fun visit(
        version: Int,
        access: Int,
        name: String?,
        signature: String?,
        superName: String?,
        interfaces: Array<out String>?
    ) {
        super.visit(version, access, name, signature, superName, interfaces)
        clazzName = name
        if (clazzName?.startsWith('L') == false) {
            // monitor-plugin 0.2.9 has a weired bug that clazzName is not
            clazzName = "L$clazzName;"
        }
    }

    override fun visitAnnotation(desc: String?, visible: Boolean): AnnotationVisitor? {
        if (desc == CLAYMORE_IMPLEMENTATION_ANNOTATION_DESC || desc == CLAYMORE_NOOP_ANNOTATION_DESC) {
            return object : AnnotationVisitor(Opcodes.ASM5, super.visitAnnotation(desc, visible)) {
                override fun visit(name: String?, value: Any?) {
                    super.visit(name, value)
                    when(desc) {
                        CLAYMORE_NOOP_ANNOTATION_DESC -> {
                            println("ClaymoreElementCollector: cannot get type ClaymoreNoop class = $clazzName, interface = $value")
                            claymoreElements.add(
                                ClaymoreElement.Noop(
                                    clazz = Type.getType(clazzName),
                                    interfaze = value as Type
                                )
                            )
                        }
                        CLAYMORE_IMPLEMENTATION_ANNOTATION_DESC -> {
                            claymoreElements.add(
                                ClaymoreElement.Impl(
                                    clazz = Type.getType(clazzName),
                                    interfaze = value as Type
                                )
                            )
                            println("ClaymoreElementCollector: cannot get type ClaymoreImpl class = $clazzName, interface = $value")
                        }
                    }
                }
            }
        }
        return super.visitAnnotation(desc, visible)
    }

    fun filterClass(fileName: String): Boolean {
        return !(!fileName.endsWith(".class")
                || fileName.contains("R$")
                || fileName.contains("R.class")
                || fileName.contains("BuildConfig.class"))
    }

    fun filterPackage(fileName: String): Boolean {
        return !(fileName.startsWith("android/support")
                || fileName.startsWith("android/arch")
                || fileName.startsWith("dagger/")
                || fileName.startsWith("java")
                || fileName.startsWith("kotlin")
                || fileName.startsWith("org"))
    }

    fun clear() {
        claymoreElements = mutableListOf()
    }
}