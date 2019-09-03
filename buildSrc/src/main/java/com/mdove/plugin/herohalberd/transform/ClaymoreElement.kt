package com.mdove.plugin.herohalberd.transform

import org.objectweb.asm.Type

sealed class ClaymoreElement(val clazz: Type, val interfaze: Type, val priority: Int) {
    class Impl(clazz: Type, interfaze: Type) : ClaymoreElement(clazz = clazz, interfaze = interfaze, priority = 1)
    class Noop(clazz: Type, interfaze: Type) : ClaymoreElement(clazz = clazz, interfaze = interfaze, priority = 0)
}