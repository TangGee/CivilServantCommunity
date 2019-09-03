package com.mdove.plugin.herohalberd.transform

import org.objectweb.asm.*
import org.objectweb.asm.Opcodes.*

class ClaymoreServiceLoaderWriter(info: List<ClaymoreElement>, cv: ClassVisitor) : ClassVisitor(Opcodes.ASM5, cv) {

    companion object {
        const val TAG = "ClaymoreServiceLoaderWriter"
    }

    val infoMap: Map<Type, List<ClaymoreElement>> = info.sortedByDescending { it.priority }.groupBy { it.interfaze }

    class CreateInstanceMethodAdapter(mv: MethodVisitor, val map: Map<Type, List<ClaymoreElement>>) : MethodVisitor(Opcodes.ASM5, mv) {

        init {
            println("ClaymoreServiceLoaderWriter: $map")
        }
        override fun visitCode() {
            visitCreateInstance()
        }

        private fun visitCreateInstance() {
            println("ClaymoreServiceLoaderWriter visitCreateInstance()")
            map.forEach { entry ->
                visitInterfaceSwitchCase(entry.key, entry.value)
            }
        }

        private fun visitInterfaceSwitchCase(type: Type, elements: List<ClaymoreElement>) {
            println("ClaymoreServiceLoaderWriter visitInterfaceSwitchCase: $type $elements")
            mv.visitVarInsn(ALOAD, 0)
            mv.visitLdcInsn(type)
            val endIfLabel = Label()
            mv.visitJumpInsn(IF_ACMPNE, endIfLabel)
            mv.visitVarInsn(ILOAD, 1)
            val switchCaseLabelArray = Array(elements.size) { Label() }
            val defaultLabel = Label()
            mv.visitLookupSwitchInsn(defaultLabel, IntArray(elements.size) { it }, switchCaseLabelArray)
            elements.forEachIndexed { index, claymoreElement ->
                visitIndexSwitchCase(claymoreElement, switchCaseLabelArray[index])
            }
            mv.visitLabel(defaultLabel)
            mv.visitInsn(Opcodes.ACONST_NULL)
            mv.visitInsn(Opcodes.ARETURN)
            mv.visitLabel(endIfLabel)
        }

        private fun visitIndexSwitchCase(element: ClaymoreElement, caseLabel: Label) {
            println("ClaymoreServiceLoaderWriter visitInterfaceSwitchCase: $element $caseLabel")
            mv.visitLabel(caseLabel)
            mv.visitTypeInsn(NEW, element.clazz.internalName)
            mv.visitInsn(DUP)
            mv.visitMethodInsn(INVOKESPECIAL, element.clazz.internalName, "<init>", "()V", false)
            mv.visitInsn(ARETURN)
        }

        override fun visitMaxs(maxStack: Int, maxLocals: Int) {
            super.visitMaxs(2, 2)
        }
    }

    class GetStaticCountMethodAdapter(mv: MethodVisitor, val map: Map<Type, List<ClaymoreElement>>) : MethodVisitor(Opcodes.ASM5, mv) {

        init {
            println("ClaymoreServiceLoaderWriter: $map")
        }
        override fun visitCode() {
            visitCreateInstance()
        }

        private fun visitCreateInstance() {
            println("ClaymoreServiceLoaderWriter visitCreateInstance()")
            map.forEach { entry ->
                visitInterfaceSwitchCase(entry.key, entry.value)
            }
            mv.visitInsn(Opcodes.ICONST_0)
            mv.visitInsn(Opcodes.IRETURN)
        }

        private fun visitInterfaceSwitchCase(type: Type, elements: List<ClaymoreElement>) {
            println("ClaymoreServiceLoaderWriter visitInterfaceSwitchCase: $type $elements")
            mv.visitVarInsn(ALOAD, 0)
            mv.visitLdcInsn(type)
            val endIfLabel = Label()
            mv.visitJumpInsn(IF_ACMPNE, endIfLabel)
            mv.visitIntInsn(Opcodes.BIPUSH, elements.size)
            mv.visitInsn(Opcodes.IRETURN)
            mv.visitLabel(endIfLabel)
        }

        override fun visitMaxs(maxStack: Int, maxLocals: Int) {
            super.visitMaxs(2, 1)
        }
    }

    override fun visitMethod(
        access: Int,
        name: String?,
        desc: String?,
        signature: String?,
        exceptions: Array<out String>?
    ): MethodVisitor? {
        println("$TAG visitMethod $name")
        if (name == "createInstanceFromStatic") {
            return CreateInstanceMethodAdapter(super.visitMethod(access, name, desc, signature, exceptions), infoMap)
        }
        if (name == "getStaticCount") {
            return GetStaticCountMethodAdapter(super.visitMethod(access, name, desc, signature, exceptions), infoMap)
        }
        return super.visitMethod(access, name, desc, signature, exceptions)
    }
}